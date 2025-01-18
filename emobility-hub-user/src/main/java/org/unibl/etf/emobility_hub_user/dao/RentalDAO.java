package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.emobility_hub_user.models.dto.PageMetadata;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;

public class RentalDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String INSERT_RENTAL = "INSERT INTO rental ("
			+ "duration, rentalEnd, rentalStart, client_id, vehicle_id, "
			+ "end_latitude, end_longitude, start_latitude, start_longitude, distance, price) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String SELECT_PAGINATED_RENTALS = "SELECT * FROM rental ORDER BY id DESC LIMIT ? OFFSET ?;";
	private static final String COUNT_TOTAL_RENTALS = "SELECT COUNT(*) FROM rental;";

	public static boolean createRental(RentalEntity rental) {
		Connection connection = null;
		boolean success = false;

		try {
			connection = connectionPool.checkOut();
			PreparedStatement stmt = connection.prepareStatement(INSERT_RENTAL);

			stmt.setDouble(1, rental.getDuration());
			stmt.setObject(2, rental.getRentalEnd());
			stmt.setObject(3, rental.getRentalStart());
			stmt.setLong(4, rental.getClientId());
			stmt.setLong(5, rental.getVehicleId());
			stmt.setDouble(6, rental.getEnd_latitude());
			stmt.setDouble(7, rental.getEnd_longitude());
			stmt.setDouble(8, rental.getStart_latitude());
			stmt.setDouble(9, rental.getStart_longitude());
			stmt.setDouble(10, rental.getDistance());
			stmt.setDouble(11, rental.getPrice());

			success = stmt.executeUpdate() > 0;

			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connectionPool.checkIn(connection);
			}
		}

		return success;
	}

	public static PaginatedResponse<RentalEntity> getPaginatedRentals(int page, int size) {
		Connection connection = null;
		PaginatedResponse<RentalEntity> response = new PaginatedResponse<>();

		try {
			connection = connectionPool.checkOut();

			// Calculate offset
			int offset = (page - 1) * size;

			// Fetch paginated rentals
			PreparedStatement stmt = connection.prepareStatement(SELECT_PAGINATED_RENTALS);
			stmt.setInt(1, size);
			stmt.setInt(2, offset);

			ResultSet rs = stmt.executeQuery();
			List<RentalEntity> rentals = new ArrayList<>();

			while (rs.next()) {
				RentalEntity rental = new RentalEntity();
				rental.setId(rs.getLong("id"));
				rental.setDuration(rs.getDouble("duration"));
				rental.setRentalEnd(rs.getTimestamp("rentalEnd").toLocalDateTime());
				rental.setRentalStart(rs.getTimestamp("rentalStart").toLocalDateTime());
				rental.setClientId(rs.getLong("client_id"));
				rental.setVehicleId(rs.getLong("vehicle_id"));
				rental.setEnd_latitude(rs.getDouble("end_latitude"));
				rental.setEnd_longitude(rs.getDouble("end_longitude"));
				rental.setStart_latitude(rs.getDouble("start_latitude"));
				rental.setStart_longitude(rs.getDouble("start_longitude"));
				rental.setDistance(rs.getDouble("distance"));
				rental.setPrice(rs.getDouble("price"));

				rentals.add(rental);
			}

			rs.close();
			stmt.close();

			// Fetch total count of rentals
			PreparedStatement countStmt = connection.prepareStatement(COUNT_TOTAL_RENTALS);
			ResultSet countRs = countStmt.executeQuery();

			long totalElements = 0;
			if (countRs.next()) {
				totalElements = countRs.getLong(1);
			}

			countRs.close();
			countStmt.close();

			// Calculate total pages
			int totalPages = (int) Math.ceil((double) totalElements / size);

			// Set response
			PageMetadata pageMetadata = new PageMetadata();
			pageMetadata.setSize(size);
			pageMetadata.setNumber(page);
			pageMetadata.setTotalElements(totalElements);
			pageMetadata.setTotalPages(totalPages);

			response.setContent(rentals);
			response.setPage(pageMetadata);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connectionPool.checkIn(connection);
			}
		}

		return response;
	}
}
