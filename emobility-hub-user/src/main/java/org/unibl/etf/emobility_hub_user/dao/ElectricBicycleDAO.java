package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.emobility_hub_user.models.dto.PageMetadata;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.ElectricBicycleEntity;

public class ElectricBicycleDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	private static final String SELECT_ELECTRIC_BICYCLES_PAGINATED = "SELECT eb.id, tv.uniqueIdentifier, m.name AS manufacturer_name, tv.model, tv.purchasePrice, "
			+ "tv.image, tv.isBroken, tv.rentPrice, eb.rangePerCharge " + "FROM electric_bicycle eb "
			+ "JOIN transport_vehicle tv ON eb.id = tv.id " + "JOIN manufacturer m ON tv.manufacturer_id = m.id "
			+ "WHERE tv.isBroken = 0 " + "ORDER BY eb.id DESC " + "LIMIT ? OFFSET ?";

	private static final String COUNT_ELECTRIC_BICYCLES = "SELECT COUNT(*) " + "FROM electric_bicycle es "
			+ "JOIN transport_vehicle tv ON es.id = tv.id " + "WHERE tv.isBroken = 0";

	public static PaginatedResponse<ElectricBicycleEntity> getElectricBicyclesPaginated(int page, int size) {
		PaginatedResponse<ElectricBicycleEntity> response = new PaginatedResponse<>();
		List<ElectricBicycleEntity> bicycles = new ArrayList<>();
		PageMetadata metadata = new PageMetadata();

		Connection connection = null;
		try {
			connection = connectionPool.checkOut();

			PreparedStatement countStmt = connection.prepareStatement(COUNT_ELECTRIC_BICYCLES);
			ResultSet countRs = countStmt.executeQuery();
			if (countRs.next()) {
				long totalElements = countRs.getLong(1);
				metadata.setTotalElements(totalElements);
				metadata.setTotalPages((int) Math.ceil((double) totalElements / size));
			}
			countStmt.close();

			PreparedStatement stmt = connection.prepareStatement(SELECT_ELECTRIC_BICYCLES_PAGINATED);
			stmt.setInt(1, size);
			stmt.setInt(2, (page - 1) * size);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ElectricBicycleEntity bicycle = new ElectricBicycleEntity();
				bicycle.setId(rs.getLong("id"));
				bicycle.setUniqueIdentifier(rs.getString("uniqueIdentifier"));
				bicycle.setManufacturerName(rs.getString("manufacturer_name"));
				bicycle.setModel(rs.getString("model"));
				bicycle.setPurchasePrice(rs.getDouble("purchasePrice"));
				bicycle.setImage(rs.getString("image"));
				bicycle.setBroken(rs.getBoolean("isBroken"));
				bicycle.setRentPrice(rs.getDouble("rentPrice"));
				bicycle.setRangePerCharge(rs.getDouble("rangePerCharge"));
				bicycles.add(bicycle);
			}
			stmt.close();

			metadata.setSize(size);
			metadata.setNumber(page);
			response.setContent(bicycles);
			response.setPage(metadata);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return response;
	}

}
