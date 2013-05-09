package org.onetwo.common.db.wheel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class ResultSetLooperHandler implements ResultSetHandler {
	
	private DataRowMapper mapper;
	
	public ResultSetLooperHandler(DataRowMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Object handleData(ResultSet rs) throws SQLException {
		List datalist = new ArrayList();
		Object value;
		Map<String, Integer> colNames = DBUtils.getColumnMeta(rs);
		while(rs.next()){
			value = mapper.mapDataRow(rs, colNames);
			if(value!=null)
				datalist.add(value);
		}
		return datalist;
	}
	
}