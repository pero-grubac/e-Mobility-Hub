package org.unibl.etf.emobility_hub_user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DAOUtil {

	public static PreparedStatement prepareStatement(Connection connection,
			String sql, boolean returnGeneratedKeys, Object... values)
			throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql,
				returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS
						: Statement.NO_GENERATED_KEYS);
		setValues(preparedStatement, values);
		return preparedStatement;
	}

	public static Double getNullableDouble(ResultSet rs, String column) throws SQLException {
		double value = rs.getDouble(column);
		return rs.wasNull() ? null : value;
	}

	public static void setValues(PreparedStatement preparedStatement,
			Object... values) throws SQLException {
		for (int i = 0; i < values.length; i++) {
			preparedStatement.setObject(i + 1, values[i]);
		}
	}
}