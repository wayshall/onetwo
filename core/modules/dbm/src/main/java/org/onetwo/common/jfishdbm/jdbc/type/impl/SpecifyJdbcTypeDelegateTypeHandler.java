package org.onetwo.common.jfishdbm.jdbc.type.impl;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.onetwo.common.jfishdbm.jdbc.type.TypeHandler;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

public class SpecifyJdbcTypeDelegateTypeHandler implements TypeHandler<Object>{
	
	final private TypeHandler<Object> typeHandler;
	final private JDBCType jdbcType;
	public SpecifyJdbcTypeDelegateTypeHandler(TypeHandler<Object> typeHandler, JDBCType jdbcType) {
		super();
		this.typeHandler = typeHandler;
		this.jdbcType = jdbcType;
	}
	@Override
	public void setParameter(PreparedStatement ps, int parameterIndex, Object value, JDBCType jdbcType) throws SQLException {
		typeHandler.setParameter(ps, parameterIndex, value, this.jdbcType);
	}
	@Override
	public Object getResult(ResultSetWrappingSqlRowSet resultSet, String columName) {
		return typeHandler.getResult(resultSet, columName);
	}
	@Override
	public Object getResult(ResultSetWrappingSqlRowSet resultSet, int columIndex) {
		return typeHandler.getResult(resultSet, columIndex);
	}

}
