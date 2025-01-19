package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.emobility_hub_user.models.dto.PageMetadata;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.ElectricScooterEntity;

public class ElectricScooterDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	private static final String SELECT_ELECTRIC_SCOOTERS_PAGINATED = "SELECT es.id, tv.uniqueIdentifier, m.name AS manufacturer_name, tv.model, tv.purchasePrice, "
			+ "tv.image, tv.isBroken, tv.rentPrice, es.maxSpeed " + "FROM electric_scooter es "
			+ "JOIN transport_vehicle tv ON es.id = tv.id " + "JOIN manufacturer m ON tv.manufacturer_id = m.id "
			+ "WHERE tv.isBroken = 0 " + "ORDER BY es.id DESC " + "LIMIT ? OFFSET ?";
	private static final String COUNT_ELECTRIC_SCOOTERS = "SELECT COUNT(*) " + "FROM electric_scooter es "
			+ "JOIN transport_vehicle tv ON es.id = tv.id " + "WHERE tv.isBroken = 0";

	public static PaginatedResponse<ElectricScooterEntity> getElectricScootersPaginated(int page, int size) {
		PaginatedResponse<ElectricScooterEntity> response = new PaginatedResponse<>();
		List<ElectricScooterEntity> scooters = new ArrayList<>();
		PageMetadata metadata = new PageMetadata();

		Connection connection = null;
		try {
			connection = connectionPool.checkOut();

			PreparedStatement countStmt = connection.prepareStatement(COUNT_ELECTRIC_SCOOTERS);
			ResultSet countRs = countStmt.executeQuery();
			if (countRs.next()) {
				long totalElements = countRs.getLong(1);
				metadata.setTotalElements(totalElements);
				metadata.setTotalPages((int) Math.ceil((double) totalElements / size));
			}
			countStmt.close();

			PreparedStatement stmt = connection.prepareStatement(SELECT_ELECTRIC_SCOOTERS_PAGINATED);
			stmt.setInt(1, size);
			stmt.setInt(2, (page - 1) * size);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ElectricScooterEntity scooter = new ElectricScooterEntity();
				scooter.setId(rs.getLong("id"));
				scooter.setUniqueIdentifier(rs.getString("uniqueIdentifier"));
				scooter.setManufacturerName(rs.getString("manufacturer_name"));
				scooter.setModel(rs.getString("model"));
				scooter.setPurchasePrice(rs.getDouble("purchasePrice"));
				scooter.setImage(rs.getString("image"));
				scooter.setBroken(rs.getBoolean("isBroken"));
				scooter.setRentPrice(rs.getDouble("rentPrice"));
				scooter.setMaxSpeed(rs.getDouble("maxSpeed"));
				scooters.add(scooter);
			}
			stmt.close();

			metadata.setSize(size);
			metadata.setNumber(page);
			response.setContent(scooters);
			response.setPage(metadata);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return response;
	}
}
