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

	private static final String COUNT_ELECTRIC_BICYCLES = "SELECT COUNT(*) FROM electric_bicycle;";
	private static final String INSERT_FAULT = "INSERT INTO fault (dateTime, description, vehicle_id, creationDateTime, updateDateTime) "
			+ "VALUES (NOW(), ?, ?, NOW(), NOW())";

	private static final String UPDATE_VEHICLE = "UPDATE transport_vehicle SET isBroken = 1 WHERE id = ?";

	public static PaginatedResponse<ElectricBicycleEntity> getElectricBicyclesPaginated(int page, int size) {
		PaginatedResponse<ElectricBicycleEntity> response = new PaginatedResponse<>();
		List<ElectricBicycleEntity> bicycles = new ArrayList<>();
		PageMetadata metadata = new PageMetadata();

		Connection connection = null;
		try {
			connection = connectionPool.checkOut();

			// Dohvati ukupni broj elemenata
			PreparedStatement countStmt = connection.prepareStatement(COUNT_ELECTRIC_BICYCLES);
			ResultSet countRs = countStmt.executeQuery();
			if (countRs.next()) {
				long totalElements = countRs.getLong(1);
				metadata.setTotalElements(totalElements);
				metadata.setTotalPages((int) Math.ceil((double) totalElements / size));
			}
			countStmt.close();

			// Dohvati podatke sa paginacijom
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

			// Popuni metapodatke i sadržaj
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

	public static boolean reportFault(long vehicleId, String description) {
		Connection connection = null;
		boolean success = false;

		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false); // Počinjemo transakciju

			// Unos greške u tabelu fault
			PreparedStatement faultStmt = connection.prepareStatement(INSERT_FAULT);
			faultStmt.setString(1, description);
			faultStmt.setLong(2, vehicleId);
			int rowsInserted = faultStmt.executeUpdate();
			faultStmt.close();

			if (rowsInserted > 0) {
				// Ažuriranje transport_vehicle tabele
				PreparedStatement vehicleStmt = connection.prepareStatement(UPDATE_VEHICLE);
				vehicleStmt.setLong(1, vehicleId);
				int rowsUpdated = vehicleStmt.executeUpdate();
				vehicleStmt.close();

				if (rowsUpdated > 0) {
					connection.commit(); // Potvrđujemo transakciju
					success = true;
				} else {
					connection.rollback(); // Vraćamo ako ažuriranje nije uspelo
				}
			} else {
				connection.rollback(); // Vraćamo ako unos greške nije uspeo
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (connection != null) {
					connection.rollback(); // Vraćamo sve u slučaju greške
				}
			} catch (Exception rollbackEx) {
				rollbackEx.printStackTrace();
			}
		} finally {
			try {
				if (connection != null) {
					connection.setAutoCommit(true); // Vraćamo na automatsko potvrđivanje
					connectionPool.checkIn(connection);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return success;
	}
}
