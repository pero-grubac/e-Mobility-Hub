package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;

public class RentalDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String INSERT_RENTAL = "INSERT INTO rental ("
	        + "duration, rentalEnd, rentalStart, client_id, vehicle_id, "
	        + "end_latitude, end_longitude, start_latitude, start_longitude, distance, price) "
	        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

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
}
