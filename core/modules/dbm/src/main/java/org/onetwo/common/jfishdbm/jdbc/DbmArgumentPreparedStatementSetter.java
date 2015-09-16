package org.onetwo.common.jfishdbm.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

public class DbmArgumentPreparedStatementSetter extends ArgumentPreparedStatementSetter {

	public DbmArgumentPreparedStatementSetter(Object[] args) {
		super(args);
	}

	protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
		if (argValue instanceof SqlParameterValue) {
			SqlParameterValue paramValue = (SqlParameterValue) argValue;
			StatementCreatorUtils.setParameterValue(ps, parameterPosition, paramValue, getActualValue(paramValue.getValue()));
		}
		else {
			StatementCreatorUtils.setParameterValue(ps, parameterPosition, SqlTypeValue.TYPE_UNKNOWN, getActualValue(argValue));
		}
	}
	
	private Object getActualValue(Object value){
		if(Enum.class.isInstance(value)){
			return ((Enum<?>)value).name();
		}
		return value;
	}
}
