package org.onetwo.common.jfishdbm.jdbc.type.impl;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.onetwo.common.jfishdbm.jdbc.type.AbstractTypeHandler;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

public class LongTypeHandler extends AbstractTypeHandler<Long> {

	public LongTypeHandler() {
	}

	@Override
	public void setParameterNotNullValue(PreparedStatement ps, int parameterIndex, Long value, JDBCType jdbcType) throws SQLException {
		ps.setLong(parameterIndex, value);
	}

	@Override
	protected Long getResultValue(ResultSetWrappingSqlRowSet resultSet, String columName) {
		return resultSet.getLong(columName);
	}

	@Override
	protected Long getResultValue(ResultSetWrappingSqlRowSet resultSet, int columIndex) {
		return resultSet.getLong(columIndex);
	}



}
