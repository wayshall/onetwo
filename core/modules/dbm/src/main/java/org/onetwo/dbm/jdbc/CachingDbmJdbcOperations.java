package org.onetwo.dbm.jdbc;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;

import org.onetwo.common.reflect.ClassIntroManager;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.spring.cache.MethodKeyGenerator;
import org.onetwo.dbm.exception.DbmException;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CachingDbmJdbcOperations implements DbmJdbcOperations {

	final private DbmJdbcTemplate dbmJdbcTemplate;
	final private KeyGenerator keyGenerator = new MethodKeyGenerator();
	final private Intro<CachingDbmJdbcOperations> intro;
	final Cache<Object, Object> queryCache = CacheBuilder.newBuilder().build();

	public CachingDbmJdbcOperations(DbmJdbcTemplate dbmJdbcTemplate) {
		super();
		this.dbmJdbcTemplate = dbmJdbcTemplate;
		this.intro = ClassIntroManager.getInstance().getIntro(CachingDbmJdbcOperations.class);
	}
	
	protected Object createCacheKey(Method method, Object...params){
		return keyGenerator.generate(this, method, params);
	}

	@SuppressWarnings("unchecked")
	protected <T> T get(Method method, Object key, Callable<T> valueLoader) throws DataAccessException {
		try {
			return (T)queryCache.get(key, valueLoader);
		} catch (ExecutionException e) {
			throw new DbmException("get data error for :"+method.getName(), e);a
		}
	};
	  

	@Override
	public <T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse) throws DataAccessException {
		Method method = intro.findMethod("query", String.class, Map.class, ResultSetExtractor.class);
		Object key = createCacheKey(method, sql, paramMap, rse);
		return get(method, key, ()->this.dbmJdbcTemplate.query(sql, paramMap, rse));
	}

	@Override
	public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
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
	public <T> List<T> queryForList(String sql, Class<T> elementType,
			Object... args) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType,
			Object... args) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args,
			RowMapper<T> rowMapper) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(String sql) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object execute(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateWith(SimpleArgsPreparedStatementCreator spsc,
			KeyHolder generatedKeyHolder) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateWith(SimpleArgsPreparedStatementCreator spsc)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateWith(SimpleArgsPreparedStatementCreator spsc,
			AroundPreparedStatementExecute action) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateWith(String sql, Object[] args,
			AroundPreparedStatementExecute action) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] batchUpdate(String sql, Map<String, ?>[] batchValues)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(String sql, Object... args) throws DataAccessException {
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
	public <T> int[][] batchUpdateWith(String sql, Collection<T[]> batchArgs,
			int batchSize) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataSource getDataSource() {
		return dbmJdbcTemplate.getDataSource();
	}
	
}
