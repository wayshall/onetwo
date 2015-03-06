package org.onetwo.plugins.dq;

import java.util.List;
import java.util.Map;

import org.onetwo.common.jdbc.NamedJdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class DqNamedJdbcTemplate implements NamedJdbcTemplate {

	private DataQueryHolderForTest dqholder;
	
	
	public DataQueryHolderForTest getDqholder() {
		return dqholder;
	}

	public void setDqholder(DataQueryHolderForTest dqholder) {
		this.dqholder = dqholder;
	}

	@Override
	public JdbcOperations getJdbcOperations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(String sql, SqlParameterSource paramSource,
			PreparedStatementCallback<T> action) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(String sql, Map<String, ?> paramMap,
			PreparedStatementCallback<T> action) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(String sql, PreparedStatementCallback<T> action)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T query(String sql, SqlParameterSource paramSource,
			ResultSetExtractor<T> rse) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T query(String sql, Map<String, ?> paramMap,
			ResultSetExtractor<T> rse) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T query(String sql, ResultSetExtractor<T> rse)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void query(String sql, SqlParameterSource paramSource,
			RowCallbackHandler rch) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void query(String sql, Map<String, ?> paramMap,
			RowCallbackHandler rch) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void query(String sql, RowCallbackHandler rch)
			throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> List<T> query(String sql, SqlParameterSource paramSource,
			RowMapper<T> rowMapper) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> query(String sql, Map<String, ?> paramMap,
			RowMapper<T> rowMapper) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T queryForObject(String sql, SqlParameterSource paramSource,
			RowMapper<T> rowMapper) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T queryForObject(String sql, Map<String, ?> paramMap,
			RowMapper<T> rowMapper) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T queryForObject(String sql, SqlParameterSource paramSource,
			Class<T> requiredType) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T queryForObject(String sql, Map<String, ?> paramMap,
			Class<T> requiredType) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> queryForMap(String sql,
			SqlParameterSource paramSource) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long queryForLong(String sql, SqlParameterSource paramSource)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long queryForLong(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryForInt(String sql, SqlParameterSource paramSource)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryForInt(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> List<T> queryForList(String sql, SqlParameterSource paramSource,
			Class<T> elementType) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> queryForList(String sql, Map<String, ?> paramMap,
			Class<T> elementType) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql,
			SqlParameterSource paramSource) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql,
			Map<String, ?> paramMap) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(String sql, SqlParameterSource paramSource)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String sql, SqlParameterSource paramSource,
			KeyHolder generatedKeyHolder) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String sql, SqlParameterSource paramSource,
			KeyHolder generatedKeyHolder, String[] keyColumnNames)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
		this.dqholder.setSql(sql);
		this.dqholder.setBatchValues(batchValues);
		return null;
	}

	@Override
	public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
		this.dqholder.setSql(sql);
		return null;
	}

	@Override
	public Object execute(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
