package org.onetwo.dbm.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

public class DbmArgumentPreparedStatementSetter extends ArgumentPreparedStatementSetter implements SqlParametersProvider {

	private final Object[] preparedStatementArgs;
	private JdbcStatementParameterSetter parameterSetter;
	
	public DbmArgumentPreparedStatementSetter(JdbcStatementParameterSetter parameterSetter, Object[] args) {
		super(args);
		this.preparedStatementArgs = args;
		this.parameterSetter = parameterSetter;
	}

	protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
		parameterSetter.setParameterValue(ps, parameterPosition, argValue);
	}

	@Override
	public Object[] getSqlParameters() {
		return preparedStatementArgs;
	}

	@Override
	public List<?> getSqlParameterList() {
		return Arrays.asList(preparedStatementArgs);
	}
	
}
