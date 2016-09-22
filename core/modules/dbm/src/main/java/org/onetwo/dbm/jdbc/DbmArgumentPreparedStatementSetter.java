package org.onetwo.dbm.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

public class DbmArgumentPreparedStatementSetter extends ArgumentPreparedStatementSetter {

	private JdbcStatementParameterSetter parameterSetter;
	
	public DbmArgumentPreparedStatementSetter(JdbcStatementParameterSetter parameterSetter, Object[] args) {
		super(args);
		this.parameterSetter = parameterSetter;
	}

	protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
		parameterSetter.setParameterValue(ps, parameterPosition, argValue);
	}
	
}
