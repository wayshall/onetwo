package org.onetwo.common.jfishdbm.jdbc.type;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

public interface TypeHandler<T> {

	public void setParameter(PreparedStatement ps, int parameterIndex, T value, JDBCType jdbcType) throws SQLException;
	
	public T getResult(ResultSetWrappingSqlRowSet resultSet, String columName);
	public T getResult(ResultSetWrappingSqlRowSet resultSet, int columIndex);

}
