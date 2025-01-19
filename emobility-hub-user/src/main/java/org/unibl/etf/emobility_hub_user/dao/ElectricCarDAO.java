package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.emobility_hub_user.models.dto.PageMetadata;
import org.unibl.etf.emobility_hub_user.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_user.models.entity.ElectricCarEntity;

public class ElectricCarDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String SELECT_ELECTRIC_CARS_PAGINATED = "SELECT ec.id, tv.uniqueIdentifier, m.name AS manufacturer_name, tv.model, tv.purchasePrice, "
			+ "tv.image, tv.isBroken, tv.rentPrice, ec.purchaseDate, ec.description " + "FROM electric_car ec "
			+ "JOIN transport_vehicle tv ON ec.id = tv.id " + "JOIN manufacturer m ON tv.manufacturer_id = m.id "
			+ "WHERE tv.isBroken = 0 " + "ORDER BY ec.id DESC LIMIT ? OFFSET ?";

	private static final String COUNT_ELECTRIC_CARS = "SELECT COUNT(*) " + "FROM electric_car es "
			+ "JOIN transport_vehicle tv ON es.id = tv.id " + "WHERE tv.isBroken = 0";

	public static PaginatedResponse<ElectricCarEntity> getElectricCarsPaginated(int page, int size) {
		PaginatedResponse<ElectricCarEntity> response = new PaginatedResponse<>();
		List<ElectricCarEntity> cars = new ArrayList<>();
		PageMetadata metadata = new PageMetadata();

		Connection connection = null;
		try {
			connection = connectionPool.checkOut();

			PreparedStatement countStmt = connection.prepareStatement(COUNT_ELECTRIC_CARS);
			ResultSet countRs = countStmt.executeQuery();
			if (countRs.next()) {
				long totalElements = countRs.getLong(1);
				metadata.setTotalElements(totalElements);
				metadata.setTotalPages((int) Math.ceil((double) totalElements / size));
			}
			countStmt.close();

			PreparedStatement stmt = connection.prepareStatement(SELECT_ELECTRIC_CARS_PAGINATED);
			stmt.setInt(1, size);
			stmt.setInt(2, (page - 1) * size);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ElectricCarEntity car = new ElectricCarEntity();
				car.setId(rs.getLong("id"));
				car.setUniqueIdentifier(rs.getString("uniqueIdentifier"));
				car.setManufacturerName(rs.getString("manufacturer_name"));
				car.setModel(rs.getString("model"));
				car.setPurchasePrice(rs.getDouble("purchasePrice"));
				car.setImage(rs.getString("image"));
				car.setBroken(rs.getBoolean("isBroken"));
				car.setRentPrice(rs.getDouble("rentPrice"));
				car.setPurchaseDate(rs.getTimestamp("purchaseDate").toLocalDateTime());
				car.setDescription(rs.getString("description"));
				cars.add(car);
			}
			stmt.close();

			metadata.setSize(size);
			metadata.setNumber(page);
			response.setContent(cars);
			response.setPage(metadata);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return response;
	}

}
