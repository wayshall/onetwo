package org.onetwo.common.db.wheel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


@SuppressWarnings("unchecked")
public class SingleResultHandler implements ResultSetHandler {
	
	private DataRowMapper mapper;
	
	public SingleResultHandler(DataRowMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Object handleData(ResultSet rs) throws SQLException {
		Object value = null;
		Map<String, Integer> colNames = DBUtils.getColumnMeta(rs);
		if(rs.next()){
			value = mapper.mapDataRow(rs, colNames);
		}
		return value;
	}
	
}