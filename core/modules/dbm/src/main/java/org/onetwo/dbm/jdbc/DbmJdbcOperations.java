package org.onetwo.dbm.jdbc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

public interface DbmJdbcOperations /*extends JdbcOperations*/ {
	
	<T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse) throws DataAccessException;
	
	<T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException;
	
	<T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException;
	
//	<T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException;
	/***
	 * 
	 * @param sql
	 * @param elementType SingleColumnRowMapper
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException;

	/***
	 * 
	 * @param sql
	 * @param requiredType SingleColumnRowMapper
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	<T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException;
	
	<T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;
	
	<T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;

	void execute(String sql) throws DataAccessException;
	Object execute(String sql, Map<String, ?> paramMap) throws DataAccessException ;
	
	int updateWith(final SimpleArgsPreparedStatementCreator spsc, final KeyHolder generatedKeyHolder) throws DataAccessException;

	int updateWith(final SimpleArgsPreparedStatementCreator spsc) throws DataAccessException;

	int updateWith(final SimpleArgsPreparedStatementCreator spsc, final AroundPreparedStatementExecute action) throws DataAccessException;

	int updateWith(String sql, Object[] args, final AroundPreparedStatementExecute action) throws DataAccessException;

	int[] batchUpdate(String sql, Map<String, ?>[] batchValues) throws DataAccessException;

	int update(String sql, Object... args) throws DataAccessException;
	
	int update(String sql, Map<String, ?> paramMap) throws DataAccessException;
	
	<T> int[][] batchUpdateWith(String sql, Collection<T[]> batchArgs, int batchSize) throws DataAccessException;
	
	void setDataSource(DataSource dataSource);

	DataSource getDataSource();
	
//	DbmNamedJdbcOperations getDbmNamedJdbcOperations();
	
}