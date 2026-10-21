package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.models.dto.PageMetadata;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.DocumentType;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;
import org.unibl.etf.emobility_hub_user.models.entity.VehicleType;
import org.unibl.etf.emobility_hub_user.ride.RideCalculator;
import org.unibl.etf.emobility_hub_user.ride.RideException;

public class RentalDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	private static final Logger logger = Logger.getLogger(RentalDAO.class.getName());

	private static final String SELECT_RENTAL = "SELECT r.*, tv.model, tv.uniqueIdentifier, tv.image, tv.rentPrice, "
			+ TransportVehicleDAO.VEHICLE_TYPE_SQL + " AS vehicle_type FROM rental r "
			+ "JOIN transport_vehicle tv ON tv.id = r.vehicle_id " + TransportVehicleDAO.VEHICLE_TYPE_JOINS;

	private static final String SELECT_BY_ID_AND_CLIENT = SELECT_RENTAL + "WHERE r.id = ? AND r.client_id = ?";
	private static final String SELECT_ACTIVE_BY_CLIENT = SELECT_RENTAL
			+ "WHERE r.client_id = ? AND r.rentalEnd IS NULL ORDER BY r.id DESC LIMIT 1";
	private static final String SELECT_PAGINATED_BY_CLIENT = SELECT_RENTAL
			+ "WHERE r.client_id = ? ORDER BY r.rentalEnd IS NULL DESC, r.id DESC LIMIT ? OFFSET ?";
	private static final String COUNT_BY_CLIENT = "SELECT COUNT(*) FROM rental WHERE client_id = ?";
	private static final String COUNT_ACTIVE_BY_CLIENT = "SELECT COUNT(*) FROM rental WHERE client_id = ? AND rentalEnd IS NULL";

	// Only a free vehicle can be taken; checking and taking happen in one statement.
	private static final String TAKE_VEHICLE = "UPDATE transport_vehicle SET isRented = 1 "
			+ "WHERE id = ? AND isRented = 0 AND isBroken = 0";
	private static final String INSERT_RIDE = "INSERT INTO rental (rentalStart, start_latitude, start_longitude, "
			+ "client_id, vehicle_id, documentType, documentNumber, driverLicenseNumber, paymentCardLast4) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String LOCK_ACTIVE_RIDE = "SELECT r.rentalStart, r.start_latitude, r.start_longitude, "
			+ "r.vehicle_id, tv.rentPrice FROM rental r JOIN transport_vehicle tv ON tv.id = r.vehicle_id "
			+ "WHERE r.id = ? AND r.client_id = ? AND r.rentalEnd IS NULL FOR UPDATE";
	private static final String COMPLETE_RIDE = "UPDATE rental SET rentalEnd = ?, end_latitude = ?, end_longitude = ?, "
			+ "duration = ?, distance = ?, price = ? WHERE id = ?";
	private static final String RELEASE_VEHICLE = "UPDATE transport_vehicle SET isRented = 0, latitude = ?, longitude = ? "
			+ "WHERE id = ?";

	// Creates the rental and marks the vehicle rented, both or neither.
	public static RentalEntity startRide(RentalEntity ride) throws RideException {
		Connection connection = null;
		Long rentalId = null;

		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);

			if (count(connection, COUNT_ACTIVE_BY_CLIENT, ride.getClientId()) > 0) {
				throw new RideException("You already have a ride in progress.");
			}

			try (PreparedStatement take = connection.prepareStatement(TAKE_VEHICLE)) {
				take.setLong(1, ride.getVehicleId());
				if (take.executeUpdate() == 0) {
					throw new RideException("This vehicle is no longer available.");
				}
			}

			try (PreparedStatement insert = DAOUtil.prepareStatement(connection, INSERT_RIDE, true,
					Timestamp.valueOf(ride.getRentalStart()), ride.getStartLatitude(), ride.getStartLongitude(),
					ride.getClientId(), ride.getVehicleId(),
					ride.getDocumentType() == null ? null : ride.getDocumentType().name(), ride.getDocumentNumber(),
					ride.getDriverLicenseNumber(), ride.getPaymentCardLast4())) {
				insert.executeUpdate();
				try (ResultSet keys = insert.getGeneratedKeys()) {
					if (keys.next()) {
						rentalId = keys.getLong(1);
					}
				}
			}

			connection.commit();
		} catch (RideException e) {
			rollback(connection);
			throw e;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			rollback(connection);
			throw new RideException("The ride could not be started. Please try again.");
		} finally {
			release(connection);
		}

		return getByIdForClient(rentalId, ride.getClientId());
	}

	// Stops the clock, charges the ride and parks the vehicle at the drop-off point.
	public static RentalEntity finishRide(long rentalId, long clientId, double endLatitude, double endLongitude,
			LocalDateTime end) throws RideException {
		Connection connection = null;

		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);

			LocalDateTime start;
			double startLatitude;
			double startLongitude;
			long vehicleId;
			double rentPrice;
			try (PreparedStatement lock = connection.prepareStatement(LOCK_ACTIVE_RIDE)) {
				lock.setLong(1, rentalId);
				lock.setLong(2, clientId);
				try (ResultSet rs = lock.executeQuery()) {
					if (!rs.next()) {
						throw new RideException("This ride is not in progress.");
					}
					start = rs.getTimestamp("rentalStart").toLocalDateTime();
					startLatitude = rs.getDouble("start_latitude");
					startLongitude = rs.getDouble("start_longitude");
					vehicleId = rs.getLong("vehicle_id");
					rentPrice = rs.getDouble("rentPrice");
				}
			}

			double distance = RideCalculator
					.round2(RideCalculator.distanceKm(startLatitude, startLongitude, endLatitude, endLongitude));
			double hours = RideCalculator.billableHours(start, end);
			double price = RideCalculator.price(rentPrice, start, end);

			try (PreparedStatement complete = DAOUtil.prepareStatement(connection, COMPLETE_RIDE, false,
					Timestamp.valueOf(end), endLatitude, endLongitude, hours, distance, price, rentalId);
					PreparedStatement free = DAOUtil.prepareStatement(connection, RELEASE_VEHICLE, false, endLatitude,
							endLongitude, vehicleId)) {
				complete.executeUpdate();
				free.executeUpdate();
			}

			connection.commit();
		} catch (RideException e) {
			rollback(connection);
			throw e;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			rollback(connection);
			throw new RideException("The ride could not be finished. Please try again.");
		} finally {
			release(connection);
		}

		return getByIdForClient(rentalId, clientId);
	}

	public static RentalEntity getActiveRide(long clientId) {
		return querySingle(SELECT_ACTIVE_BY_CLIENT, clientId);
	}

	// A client can only see their own rentals.
	public static RentalEntity getByIdForClient(Long rentalId, long clientId) {
		if (rentalId == null) {
			return null;
		}
		return querySingle(SELECT_BY_ID_AND_CLIENT, rentalId, clientId);
	}

	public static PaginatedResponse<RentalEntity> getPaginatedRentalsByClient(int page, int size, long clientId) {
		Connection connection = null;
		PaginatedResponse<RentalEntity> response = new PaginatedResponse<>();

		try {
			connection = connectionPool.checkOut();
			List<RentalEntity> rentals = new ArrayList<>();
			try (PreparedStatement stmt = DAOUtil.prepareStatement(connection, SELECT_PAGINATED_BY_CLIENT, false,
					clientId, size, (page - 1) * size); ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rentals.add(map(rs));
				}
			}

			long totalElements = count(connection, COUNT_BY_CLIENT, clientId);
			PageMetadata pageMetadata = new PageMetadata();
			pageMetadata.setSize(size);
			pageMetadata.setNumber(page);
			pageMetadata.setTotalElements(totalElements);
			pageMetadata.setTotalPages((int) Math.ceil((double) totalElements / size));

			response.setContent(rentals);
			response.setPage(pageMetadata);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			connectionPool.checkIn(connection);
		}

		return response;
	}

	private static RentalEntity querySingle(String sql, Object... params) {
		Connection connection = null;
		RentalEntity rental = null;

		try {
			connection = connectionPool.checkOut();
			try (PreparedStatement stmt = DAOUtil.prepareStatement(connection, sql, false, params);
					ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					rental = map(rs);
				}
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			connectionPool.checkIn(connection);
		}

		return rental;
	}

	private static long count(Connection connection, String sql, long clientId) throws SQLException {
		try (PreparedStatement stmt = DAOUtil.prepareStatement(connection, sql, false, clientId);
				ResultSet rs = stmt.executeQuery()) {
			return rs.next() ? rs.getLong(1) : 0;
		}
	}

	private static RentalEntity map(ResultSet rs) throws SQLException {
		RentalEntity rental = new RentalEntity();
		rental.setId(rs.getLong("id"));
		rental.setClientId(rs.getLong("client_id"));
		rental.setVehicleId(rs.getLong("vehicle_id"));
		rental.setRentalStart(rs.getTimestamp("rentalStart").toLocalDateTime());
		Timestamp end = rs.getTimestamp("rentalEnd");
		rental.setRentalEnd(end == null ? null : end.toLocalDateTime());
		rental.setStartLatitude(rs.getDouble("start_latitude"));
		rental.setStartLongitude(rs.getDouble("start_longitude"));
		rental.setEndLatitude(DAOUtil.getNullableDouble(rs, "end_latitude"));
		rental.setEndLongitude(DAOUtil.getNullableDouble(rs, "end_longitude"));
		rental.setDuration(DAOUtil.getNullableDouble(rs, "duration"));
		rental.setDistance(DAOUtil.getNullableDouble(rs, "distance"));
		rental.setPrice(DAOUtil.getNullableDouble(rs, "price"));
		String documentType = rs.getString("documentType");
		rental.setDocumentType(documentType == null ? null : DocumentType.valueOf(documentType));
		rental.setDocumentNumber(rs.getString("documentNumber"));
		rental.setDriverLicenseNumber(rs.getString("driverLicenseNumber"));
		rental.setPaymentCardLast4(rs.getString("paymentCardLast4"));
		rental.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
		rental.setVehicleModel(rs.getString("model"));
		rental.setVehicleUniqueIdentifier(rs.getString("uniqueIdentifier"));
		rental.setVehicleImage(rs.getString("image"));
		rental.setRentPrice(rs.getDouble("rentPrice"));
		return rental;
	}

	private static void rollback(Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.rollback();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
	}

	// Pooled connections go back in auto-commit mode.
	private static void release(Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		connectionPool.checkIn(connection);
	}
}
