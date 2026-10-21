package org.unibl.etf.emobility_hub_user.dao;

import java.util.logging.Logger;
import java.sql.*;
import java.util.*;

public class ConnectionPool {
	private static final Logger logger = Logger.getLogger(ConnectionPool.class.getName());
	public static ConnectionPool getConnectionPool() {
		return connectionPool;
	}

	private static ConnectionPool connectionPool;

	static {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("org.unibl.etf.emobility_hub_user.dao.ConnectionPool");
		String jdbcURL = setting("db.url", "DB_URL", bundle.getString("jdbcURL"));
		String username = setting("db.username", "DB_USERNAME", bundle.getString("username"));
		String password = setting("db.password", "DB_PASSWORD", bundle.getString("password"));
		String driver = bundle.getString("driver");
		int preconnectCount = 0;
		int maxIdleConnections = 10;
		int maxConnections = 10;
		try {
			Class.forName(driver);
			preconnectCount = Integer.parseInt(
					bundle.getString("preconnectCount"));
			maxIdleConnections = Integer.parseInt(
					bundle.getString("maxIdleConnections"));
			maxConnections = Integer.parseInt(
					bundle.getString("maxConnections"));
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
		}
		try {
			connectionPool = new ConnectionPool(
					jdbcURL, username, password,
					preconnectCount, maxIdleConnections,
					maxConnections);
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
		}
	}

	// A JVM system property wins over an environment variable, which wins over
	// ConnectionPool.properties; Docker sets DB_URL, DB_USERNAME and DB_PASSWORD.
	static String setting(String property, String env, String fallback) {
		String value = System.getProperty(property);
		if (value == null || value.isEmpty()) {
			value = System.getenv(env);
		}
		return value == null || value.isEmpty() ? fallback : value;
	}

	protected ConnectionPool(String aJdbcURL, String aUsername,
			String aPassword, int aPreconnectCount,
			int aMaxIdleConnections,
			int aMaxConnections)
			throws ClassNotFoundException, SQLException {

		freeConnections = new Vector<Connection>();
		usedConnections = new Vector<Connection>();
		jdbcURL = aJdbcURL;
		username = aUsername;
		password = aPassword;
		preconnectCount = aPreconnectCount;
		maxIdleConnections = aMaxIdleConnections;
		maxConnections = aMaxConnections;

		for (int i = 0; i < preconnectCount; i++) {
			Connection conn = DriverManager.getConnection(
					jdbcURL, username, password);
			conn.setAutoCommit(true);
			freeConnections.addElement(conn);
		}
		connectCount = preconnectCount;
	}

	public synchronized Connection checkOut()
			throws SQLException {

		Connection conn = null;
		if (freeConnections.size() > 0) {
			conn = (Connection) freeConnections.elementAt(0);
			freeConnections.removeElementAt(0);
			usedConnections.addElement(conn);
		} else {
			if (connectCount < maxConnections) {
				conn = DriverManager.getConnection(
						jdbcURL, username, password);
				usedConnections.addElement(conn);
				connectCount++;
			} else {
				try {
					wait();
					conn = (Connection) freeConnections.elementAt(0);
					freeConnections.removeElementAt(0);
					usedConnections.addElement(conn);
				} catch (InterruptedException ex) {
					logger.severe(ex.getMessage());
				}
			}
		}
		return conn;
	}

	public synchronized void checkIn(Connection aConn) {
		if (aConn == null)
			return;
		if (usedConnections.removeElement(aConn)) {
			freeConnections.addElement(aConn);
			while (freeConnections.size() > maxIdleConnections) {
				int lastOne = freeConnections.size() - 1;
				Connection conn = (Connection) freeConnections.elementAt(lastOne);
				try {
					conn.close();
				} catch (SQLException ignored) {
					// The idle connection is dropped from the pool either way.
				}
				freeConnections.removeElementAt(lastOne);
			}
			notify();
		}
	}

	private String jdbcURL;
	private String username;
	private String password;
	private int preconnectCount;
	private int connectCount;
	private int maxIdleConnections;
	private int maxConnections;
	private Vector<Connection> usedConnections;
	private Vector<Connection> freeConnections;
}
