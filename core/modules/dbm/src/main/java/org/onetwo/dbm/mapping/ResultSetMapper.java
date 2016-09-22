package org.onetwo.dbm.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.onetwo.common.utils.map.CaseInsensitiveMap;


public interface ResultSetMapper {
	@SuppressWarnings("rawtypes")
	public CaseInsensitiveMap map(ResultSet rs, CaseInsensitiveMap<?, ?> map)throws SQLException ;
}
