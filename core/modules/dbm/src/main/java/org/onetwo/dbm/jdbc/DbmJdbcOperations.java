package org.onetwo.dbm.jdbc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.dbm.annotation.DbmJdbcOperationMark;
import org.onetwo.dbm.core.DbmJdbcOperationType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

public interface DbmJdbcOperations /*extends JdbcOperations*/ {
	
	@DbmJdbcOperationMark(type=DbmJdbcOperationType.QUERY)
	<T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.QUERY)
	<T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.QUERY)
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
	@DbmJdbcOperationMark(type=DbmJdbcOperationType.QUERY)
	<T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException;

	/***
	 * 
	 * @param sql
	 * @param requiredType SingleColumnRowMapper
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	@DbmJdbcOperationMark(type=DbmJdbcOperationType.QUERY)
	<T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.QUERY)
	<T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.QUERY)
	<T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.EXECUTE)
	void execute(String sql) throws DataAccessException;
	Object execute(String sql, Map<String, ?> paramMap) throws DataAccessException ;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.UPDATE)
	int updateWith(final SimpleArgsPreparedStatementCreator spsc, final KeyHolder generatedKeyHolder) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.UPDATE)
	int updateWith(final SimpleArgsPreparedStatementCreator spsc) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.UPDATE)
	int updateWith(final SimpleArgsPreparedStatementCreator spsc, final AroundPreparedStatementExecute action) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.UPDATE)
	int updateWith(String sql, Object[] args, final AroundPreparedStatementExecute action) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.BATCH_UPDATE)
	int[] batchUpdate(String sql, Map<String, ?>[] batchValues) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.UPDATE)
	int update(String sql, Object... args) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.UPDATE)
	int update(String sql, Map<String, ?> paramMap) throws DataAccessException;

	@DbmJdbcOperationMark(type=DbmJdbcOperationType.BATCH_UPDATE)
	<T> int[][] batchUpdateWith(String sql, Collection<T[]> batchArgs, int batchSize) throws DataAccessException;
	
//	void setDataSource(DataSource dataSource);

	DataSource getDataSource();
	
//	DbmNamedJdbcOperations getDbmNamedJdbcOperations();

}