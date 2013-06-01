package org.onetwo.common.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

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
	
	private JdbcUtils(){}

}
