package org.onetwo.common.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.utils.StringUtils;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.util.Assert;

final public class JdbcUtils {
	
	public static void setValues(PreparedStatement ps, Object[] args) throws SQLException {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				doSetValue(ps, i + 1, arg);
			}
		}
	}

	public static void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
		if (argValue instanceof SqlParameterValue) {
			SqlParameterValue paramValue = (SqlParameterValue) argValue;
			StatementCreatorUtils.setParameterValue(ps, parameterPosition, paramValue, paramValue.getValue());
		}
		else {
			StatementCreatorUtils.setParameterValue(ps, parameterPosition, SqlTypeValue.TYPE_UNKNOWN, argValue);
		}
	}
	
	public static DataBase getDataBase(DataSource dataSource) {
		Assert.notNull(dataSource, "datasource can not be null");
		//从DataSource中取出jdbcUrl.
		String jdbcUrl = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (connection == null) {
				throw new IllegalStateException("Connection returned by DataSource [" + dataSource + "] was null");
			}
			jdbcUrl = connection.getMetaData().getURL();
		} catch (SQLException e) {
			throw new RuntimeException("Could not get database url", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}

		return getDataBase(jdbcUrl);
	}
	

	public static DataBase getDataBase(String jdbcUrl) {
		if(StringUtils.isBlank(jdbcUrl))
			throw new IllegalArgumentException("Unknown Database : " + jdbcUrl);
		
		jdbcUrl = jdbcUrl.toLowerCase();
		if (jdbcUrl.contains(":h2:")) {
			return DataBase.H2;
		} else if (jdbcUrl.contains(":mysql:")) {
			return DataBase.MySQL;
		} else if (jdbcUrl.contains(":sqlserver:")) {
			return DataBase.Sqlserver;
		} else if (jdbcUrl.contains(":oracle:")) {
			return DataBase.Oracle;
		} else {
			for(DataBase db : DataBase.values()){
				if(jdbcUrl.contains(":"+db.toString().toLowerCase()+":"))
					return db;
			}
			throw new IllegalArgumentException("Unknown Database config : " + jdbcUrl);
		}
	}
	
	private JdbcUtils(){}

}
