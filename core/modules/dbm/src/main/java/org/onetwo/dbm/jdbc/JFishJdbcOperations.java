package org.onetwo.dbm.jdbc;

import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.KeyHolder;

public interface JFishJdbcOperations extends JdbcOperations {

	public int updateWith(final SimpleArgsPreparedStatementCreator spsc, final KeyHolder generatedKeyHolder) throws DataAccessException;

	public int updateWith(final SimpleArgsPreparedStatementCreator spsc) throws DataAccessException;

	public int updateWith(final SimpleArgsPreparedStatementCreator spsc, final AroundPreparedStatementExecute action) throws DataAccessException;

	public int updateWith(String sql, Object[] args, final AroundPreparedStatementExecute action) throws DataAccessException;

//	public <T> List<T> fqueryWith(String sql, Class<T> entityClass, Object... args);
	
	public <T> int[][] batchUpdateWith(String sql, Collection<T[]> batchArgs, int batchSize) throws DataAccessException;
	
	public void setDataSource(DataSource dataSource);

	public DataSource getDataSource();
	
}