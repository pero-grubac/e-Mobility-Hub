package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FaultDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String INSERT_FAULT = "INSERT INTO fault (dateTime, description, vehicle_id, creationDateTime, updateDateTime) "
			+ "VALUES (NOW(), ?, ?, NOW(), NOW())";
	private static final String UPDATE_VEHICLE = "UPDATE transport_vehicle SET isBroken = 1 WHERE id = ?";

	public static boolean reportFault(long vehicleId, String description) {
		Connection connection = null;
		boolean success = false;

		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false); 

			PreparedStatement faultStmt = connection.prepareStatement(INSERT_FAULT);
			faultStmt.setString(1, description);
			faultStmt.setLong(2, vehicleId);
			int rowsInserted = faultStmt.executeUpdate();
			faultStmt.close();

			if (rowsInserted > 0) {
				PreparedStatement vehicleStmt = connection.prepareStatement(UPDATE_VEHICLE);
				vehicleStmt.setLong(1, vehicleId);
				int rowsUpdated = vehicleStmt.executeUpdate();
				vehicleStmt.close();

				if (rowsUpdated > 0) {
					connection.commit(); 
					success = true;
				} else {
					connection.rollback(); 
				}
			} else {
				connection.rollback(); 
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (connection != null) {
					connection.rollback(); 
				}
			} catch (Exception rollbackEx) {
				rollbackEx.printStackTrace();
			}
		} finally {
			try {
				if (connection != null) {
					connection.setAutoCommit(true);
					connectionPool.checkIn(connection);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return success;
	}
}
