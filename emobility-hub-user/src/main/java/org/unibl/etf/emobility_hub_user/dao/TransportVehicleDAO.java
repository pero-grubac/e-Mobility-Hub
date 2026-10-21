package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.models.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub_user.models.entity.VehicleType;

public class TransportVehicleDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	private static final Logger logger = Logger.getLogger(TransportVehicleDAO.class.getName());

	// The vehicle's type is whichever subtype table holds its id.
	static final String VEHICLE_TYPE_SQL = "CASE WHEN ec.id IS NOT NULL THEN 'CAR' "
			+ "WHEN eb.id IS NOT NULL THEN 'BICYCLE' ELSE 'SCOOTER' END";
	static final String VEHICLE_TYPE_JOINS = "LEFT JOIN electric_car ec ON ec.id = tv.id "
			+ "LEFT JOIN electric_bicycle eb ON eb.id = tv.id ";

	private static final String SELECT_BY_ID = "SELECT tv.id, tv.uniqueIdentifier, tv.model, tv.image, tv.rentPrice, "
			+ "tv.latitude, tv.longitude, tv.isBroken, tv.isRented, m.name AS manufacturer_name, "
			+ VEHICLE_TYPE_SQL + " AS vehicle_type FROM transport_vehicle tv "
			+ "JOIN manufacturer m ON tv.manufacturer_id = m.id " + VEHICLE_TYPE_JOINS + "WHERE tv.id = ?";

	public static TransportVehicleEntity getById(long vehicleId) {
		Connection connection = null;
		TransportVehicleEntity vehicle = null;

		try {
			connection = connectionPool.checkOut();
			try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
				stmt.setLong(1, vehicleId);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						vehicle = new TransportVehicleEntity();
						vehicle.setId(rs.getLong("id"));
						vehicle.setType(VehicleType.valueOf(rs.getString("vehicle_type")));
						vehicle.setUniqueIdentifier(rs.getString("uniqueIdentifier"));
						vehicle.setManufacturerName(rs.getString("manufacturer_name"));
						vehicle.setModel(rs.getString("model"));
						vehicle.setImage(rs.getString("image"));
						vehicle.setRentPrice(rs.getDouble("rentPrice"));
						vehicle.setLatitude(DAOUtil.getNullableDouble(rs, "latitude"));
						vehicle.setLongitude(DAOUtil.getNullableDouble(rs, "longitude"));
						vehicle.setBroken(rs.getBoolean("isBroken"));
						vehicle.setRented(rs.getBoolean("isRented"));
					}
				}
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			connectionPool.checkIn(connection);
		}

		return vehicle;
	}
}
