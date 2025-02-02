package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	private static final String SELECT_PAGINATED_RENTALS_BY_CLIENT = "SELECT * FROM rental WHERE client_id = ? ORDER BY id DESC LIMIT ? OFFSET ?;";
	private static final String COUNT_TOTAL_RENTALS_BY_CLIENT = "SELECT COUNT(*) FROM rental WHERE client_id = ?;";
	private static final String GET_LAST_INSERTED_RENTAL = "SELECT * FROM rental WHERE id = LAST_INSERT_ID();";
    private static final String GET_RENTAL_BY_ID = "SELECT * FROM rental WHERE id = ?";

	public static RentalEntity createRental(RentalEntity rental) {
	    Connection connection = null;
	    RentalEntity newRental = null;

	    try {
	        connection = connectionPool.checkOut();
	        PreparedStatement stmt = connection.prepareStatement(INSERT_RENTAL, Statement.RETURN_GENERATED_KEYS);

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

	        int affectedRows = stmt.executeUpdate();

	        if (affectedRows > 0) {
	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                long rentalId = generatedKeys.getLong(1);

	                PreparedStatement getRentalStmt = connection.prepareStatement(GET_LAST_INSERTED_RENTAL);
	                ResultSet rs = getRentalStmt.executeQuery();

	                if (rs.next()) {
	                    newRental = new RentalEntity();
	                    newRental.setId(rs.getLong("id"));
	                    newRental.setVehicleId(rs.getLong("vehicle_id"));
	                    newRental.setClientId(rs.getLong("client_id"));
	                    newRental.setRentalStart(rs.getTimestamp("rentalStart").toLocalDateTime());
	                    newRental.setRentalEnd(rs.getTimestamp("rentalEnd").toLocalDateTime());
	                    newRental.setDuration(rs.getDouble("duration"));
	                    newRental.setStart_latitude(rs.getDouble("start_latitude"));
	                    newRental.setStart_longitude(rs.getDouble("start_longitude"));
	                    newRental.setEnd_latitude(rs.getDouble("end_latitude"));
	                    newRental.setEnd_longitude(rs.getDouble("end_longitude"));
	                    newRental.setDistance(rs.getDouble("distance"));
	                    newRental.setPrice(rs.getDouble("price"));
	                }
	                rs.close();
	                getRentalStmt.close();
	            }
	            generatedKeys.close();
	        }
	        stmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (connection != null) {
	            connectionPool.checkIn(connection);
	        }
	    }

	    return newRental;
	}

	public static PaginatedResponse<RentalEntity> getPaginatedRentalsByClient(int page, int size, long clientId) {
		Connection connection = null;
		PaginatedResponse<RentalEntity> response = new PaginatedResponse<>();

		try {
			connection = connectionPool.checkOut();

			int offset = (page - 1) * size;

			PreparedStatement stmt = connection.prepareStatement(SELECT_PAGINATED_RENTALS_BY_CLIENT);
			stmt.setLong(1, clientId);
			stmt.setInt(2, size);
			stmt.setInt(3, offset);

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

			PreparedStatement countStmt = connection.prepareStatement(COUNT_TOTAL_RENTALS_BY_CLIENT);
			countStmt.setLong(1, clientId);
			ResultSet countRs = countStmt.executeQuery();

			long totalElements = 0;
			if (countRs.next()) {
				totalElements = countRs.getLong(1);
			}

			countRs.close();
			countStmt.close();

			int totalPages = (int) Math.ceil((double) totalElements / size);

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

	 public static RentalEntity getById(Long id) {
	        Connection connection = null;
	        RentalEntity rental = null;

	        try {
	            connection = connectionPool.checkOut();
	            PreparedStatement stmt = connection.prepareStatement(GET_RENTAL_BY_ID);
	            stmt.setLong(1, id);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                rental = new RentalEntity();
	                rental.setId(rs.getLong("id"));
	                rental.setVehicleId(rs.getLong("vehicle_id"));
	                rental.setClientId(rs.getLong("client_id"));
	                rental.setRentalStart(rs.getTimestamp("rentalStart").toLocalDateTime());
	                rental.setRentalEnd(rs.getTimestamp("rentalEnd").toLocalDateTime());
	                rental.setDuration(rs.getDouble("duration"));
	                rental.setStart_latitude(rs.getDouble("start_latitude"));
	                rental.setStart_longitude(rs.getDouble("start_longitude"));
	                rental.setEnd_latitude(rs.getDouble("end_latitude"));
	                rental.setEnd_longitude(rs.getDouble("end_longitude"));
	                rental.setDistance(rs.getDouble("distance"));
	                rental.setPrice(rs.getDouble("price"));
	            }

	            rs.close();
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (connection != null) {
	                connectionPool.checkIn(connection);
	            }
	        }

	        return rental;
	    }
}
