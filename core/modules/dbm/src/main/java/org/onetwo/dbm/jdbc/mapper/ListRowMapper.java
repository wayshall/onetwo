package org.onetwo.dbm.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class ListRowMapper implements RowMapper<List<?>> {

	@Override
	public List<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Object> rowlist = new ArrayList<Object>(columnCount);
		Object obj = null;
		for (int i = 1; i <= columnCount; i++) {
			obj = JdbcUtils.getResultSetValue(rs, i);
			rowlist.add(obj);
		}
		return rowlist;
	}

}
