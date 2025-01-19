package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransportVehicleDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String SELECT_IMAGE_BY_ID = "SELECT image FROM transport_vehicle WHERE id = ?";
	private static final String SELECT_RENT_PRICE = "SELECT rentPrice FROM transport_vehicle WHERE id = ?";

	public static String getImageById(long vehicleId) {
		Connection connection = null;
		String imagePath = null;

		try {
			connection = connectionPool.checkOut();
			PreparedStatement stmt = connection.prepareStatement(SELECT_IMAGE_BY_ID);
			stmt.setLong(1, vehicleId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				imagePath = rs.getString("image");
			}

			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return imagePath;
	}

	public static double getRentPrice(long vehicleId) {
		Connection connection = null;
		double rentPrice = 0;

		try {
			connection = connectionPool.checkOut();
			PreparedStatement stmt = connection.prepareStatement(SELECT_RENT_PRICE);
			stmt.setLong(1, vehicleId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				rentPrice = rs.getDouble("rentPrice");
			}

			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return rentPrice;
	}
}
