package org.onetwo.dbm.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.jdbc.mapper.EntryRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;

public class EntryRowMapperMapExtractor<K, V> extends AbstractResultSetExtractor<V> implements ResultSetExtractor<Map<K, V>>{

	public EntryRowMapperMapExtractor(EntryRowMapper<V> rowMapper) {
		super(rowMapper);
	}
	
	protected Map<K, V> createMap(){
		return LangUtils.newHashMap();
	}

	@SuppressWarnings("unchecked")
	public Map<K, V> extractData(ResultSet rs) throws SQLException {
		Map<K, V> results = createMap();
		int rowNum = 0;
		V val = null;
		while (rs.next()) {
			val = this.rowMapper.mapRow(rs, rowNum++);
			results.put((K)getRowMapper().getEntry().getId(val), val);
		}
		return results;
	}
	public EntryRowMapper<V> getRowMapper() {
		return (EntryRowMapper<V>)rowMapper;
	}
}
