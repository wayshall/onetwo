package org.onetwo.common.fish.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.onetwo.common.utils.map.NonCaseMap;


public interface ResultSetMapper {
	@SuppressWarnings("rawtypes")
	public NonCaseMap map(ResultSet rs, NonCaseMap<?, ?> map)throws SQLException ;
}
