package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.unibl.etf.emobility_hub_user.beans.ClientBean;
import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;
import org.unibl.etf.emobility_hub_user.models.entity.RoleEnum;

import org.mindrot.jbcrypt.BCrypt;

public class ClientDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	private static final String SQL_SELECT_CLIENT_BY_USERNAME = "SELECT u.id AS userId, u.username, u.password, u.firstName, u.lastName, u.role, "
			+ "c.email, c.phoneNumber, c.idCardNumber, c.avatarImage, c.isDeactivated, c.isBlocked "
			+ "FROM user u JOIN client c ON u.id = c.id WHERE u.username = ? and c.isBlocked=0 and c.isDeactivated=0";

	private static final String SQL_SELECT_BY_USERNAME = "SELECT COUNT(*) AS count FROM user WHERE username = ?";
	private static final String SQL_INSERT_USER = "INSERT INTO user (username, password, firstName, lastName, role) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_INSERT_CLIENT = "INSERT INTO client (id, email, phoneNumber, idCardNumber, avatarImage, isDeactivated, isBlocked) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE_PASSWORD = "UPDATE user SET password = ? WHERE id = ?";
	private static final String SQL_UPDATE_IS_DEACTIVATED = "UPDATE client SET isDeactivated = ? WHERE id = ?";
	private static final String SQL_UPDATE_AVATAR = "UPDATE client SET avatarImage = ? WHERE id = ?";

	private static final Logger logger = Logger.getLogger(ClientBean.class.getName());

	public static ClientEntity getByUsernameAndPassword(String username, String password) {
		Connection connection = null;
		ClientEntity entity = null;
		ResultSet rs = null;

		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = connection.prepareStatement(SQL_SELECT_CLIENT_BY_USERNAME);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String hashedPassword = rs.getString("password");

				if (BCrypt.checkpw(password, hashedPassword)) {
					entity = new ClientEntity();
					entity.setId(rs.getLong("userId"));
					entity.setUsername(rs.getString("username"));
					entity.setPassword(hashedPassword);
					entity.setFirstName(rs.getString("firstName"));
					entity.setLastName(rs.getString("lastName"));
					entity.setRole(RoleEnum.valueOf(rs.getString("role")));
					entity.setEmail(rs.getString("email"));
					entity.setPhoneNumber(rs.getString("phoneNumber"));
					entity.setIdCardNumber(rs.getString("idCardNumber"));
					entity.setAvatarImage(rs.getString("avatarImage"));
					entity.setDeactivated(rs.getBoolean("isDeactivated"));
					entity.setBlocked(rs.getBoolean("isBlocked"));
				}
			}

			pstmt.close();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			connectionPool.checkIn(connection);
		}

		return entity;
	}

	public static boolean usernameExists(String username) {
		Connection connection = null;
		boolean exists = false;
		ResultSet rs = null;

		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_SELECT_BY_USERNAME, false, username);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				exists = rs.getInt("count") > 0;
			}
			pstmt.close();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			connectionPool.checkIn(connection);
		}

		return exists;
	}

	public static boolean createClient(ClientEntity client) {
		if (usernameExists(client.getUsername())) {
			return false;
		}

		Connection connection = null;
		boolean success = false;

		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			client.setRole(RoleEnum.ROLE_CLIENT);
			PreparedStatement pstmtUser = DAOUtil.prepareStatement(connection, SQL_INSERT_USER, true,
					client.getUsername(), BCrypt.hashpw(client.getPassword(), BCrypt.gensalt()), client.getFirstName(),
					client.getLastName(), client.getRole().name());

			int affectedRows = pstmtUser.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Failed to create user, no rows affected.");
			}

			ResultSet generatedKeys = pstmtUser.getGeneratedKeys();
			if (generatedKeys.next()) {
				client.setId(generatedKeys.getLong(1));
			} else {
				throw new SQLException("Failed to obtain user ID.");
			}
			pstmtUser.close();

			PreparedStatement pstmtClient = DAOUtil.prepareStatement(connection, SQL_INSERT_CLIENT, false,
					client.getId(), client.getEmail(), client.getPhoneNumber(), client.getIdCardNumber(),
					client.getAvatarImage(), client.isDeactivated(), client.isBlocked());

			pstmtClient.executeUpdate();
			pstmtClient.close();

			connection.commit();
			success = true;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
		} finally {
			connectionPool.checkIn(connection);
		}

		return success;
	}

	public static boolean updatePassword(Long userId, String newPassword) {
		Connection connection = null;
		boolean success = false;

		try {
			connection = connectionPool.checkOut();
			System.out.println(newPassword);
			PreparedStatement pstmt = connection.prepareStatement(SQL_UPDATE_PASSWORD);
			pstmt.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt())); // Hashovanje nove šifre
			pstmt.setLong(2, userId);

			int affectedRows = pstmt.executeUpdate();
			success = affectedRows > 0;

			pstmt.close();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			connectionPool.checkIn(connection);
		}

		return success;
	}

	public static boolean updateIsDeactivated(Long userId, boolean isDeactivated) {
		Connection connection = null;
		boolean success = false;

		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = connection.prepareStatement(SQL_UPDATE_IS_DEACTIVATED);
			pstmt.setBoolean(1, isDeactivated);
			pstmt.setLong(2, userId);

			int affectedRows = pstmt.executeUpdate();
			success = affectedRows > 0;

			pstmt.close();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			connectionPool.checkIn(connection);
		}

		return success;
	}

	public static boolean updateAvatar(Long userId, String avatarUrl) {
		Connection connection = null;
		boolean success = false;

		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = connection.prepareStatement(SQL_UPDATE_AVATAR);
			pstmt.setString(1, avatarUrl);
			pstmt.setLong(2, userId);

			int affectedRows = pstmt.executeUpdate();
			success = affectedRows > 0;

			pstmt.close();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			connectionPool.checkIn(connection);
		}

		return success;
	}

}
