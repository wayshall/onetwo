package org.onetwo.common.db.wheel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface DataRowMapper {
	public Object mapDataRow(ResultSet rs, Map<String, Integer> columNames) throws SQLException;
	
}
