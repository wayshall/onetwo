package org.onetwo.common.fish.spring;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class HashsetRowMapper implements RowMapper<HashSet<?>> {

	@Override
	public HashSet<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		HashSet<Object> sets = new HashSet<Object>(columnCount);
		Object obj = null;
		for (int i = 1; i <= columnCount; i++) {
			obj = JdbcUtils.getResultSetValue(rs, i);
			sets.add(obj);
		}
		return sets;
	}

}
