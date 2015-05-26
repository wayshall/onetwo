package org.onetwo.common.jfishdbm.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.core.ResultSetExtractor;

public class EntryRowMapperMapExtractor<K, V> implements ResultSetExtractor<Map<K, V>>{

	private final EntryRowMapper<V> rowMapper;

	public EntryRowMapperMapExtractor(EntryRowMapper<V> rowMapper) {
		this.rowMapper = rowMapper;
	}
	
	protected Map<K, V> createMap(){
		return LangUtils.newHashMap();
	}

	public Map<K, V> extractData(ResultSet rs) throws SQLException {
		Map<K, V> results = createMap();
		int rowNum = 0;
		V val = null;
		while (rs.next()) {
			val = this.rowMapper.mapRow(rs, rowNum++);
			results.put((K)rowMapper.getEntry().getId(val), val);
		}
		return results;
	}

}
