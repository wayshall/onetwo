package org.onetwo.common.jfishdbm.jdbc.type.impl;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.onetwo.common.jfishdbm.jdbc.type.AbstractTypeHandler;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

public class IntegerTypeHandler extends AbstractTypeHandler<Integer> {

	public IntegerTypeHandler() {
	}

	@Override
	public void setParameterNotNullValue(PreparedStatement ps, int parameterIndex, Integer value, JDBCType jdbcType) throws SQLException {
		ps.setInt(parameterIndex, value);
	}

	@Override
	protected Integer getResultValue(ResultSetWrappingSqlRowSet resultSet, String columName) {
		return resultSet.getInt(columName);
	}

	@Override
	protected Integer getResultValue(ResultSetWrappingSqlRowSet resultSet, int columIndex) {
		return resultSet.getInt(columIndex);
	}



}
