package org.onetwo.dbm.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

@SuppressWarnings("rawtypes")
public class UnknowTypeRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		RowMapper rowMapper = null;
		if(columnCount==1){
			rowMapper = new SingleColumnRowMapper();
		}else{
			rowMapper = new ColumnMapRowMapper();
		}
		return rowMapper.mapRow(rs, rowNum);
	}

}
