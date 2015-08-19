package org.onetwo.common.jfishdbm.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class ObjectArrayRowMapper implements RowMapper<Object[]> {

	@Override
	public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Object[] array = new Object[columnCount];
		Object obj = null;
		for (int i = 1; i <= columnCount; i++) {
			obj = JdbcUtils.getResultSetValue(rs, i);
			array[i-1] = obj;
		}
		return array;
	}

}
