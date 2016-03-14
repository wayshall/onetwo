package org.onetwo.common.jfishdbm.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.jfishdbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

public class JFishJdbcTemplate extends JdbcTemplate implements JFishJdbcOperations {
	
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private boolean debug;
	protected NamedJdbcTemplate namedTemplate;
	protected RowMapperFactory rowMapperFactory;

	public JFishJdbcTemplate() {
//		super(dataSource);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}


	public JFishJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.initTemplateConfig();
	}

	protected void initTemplateConfig() {
		if(namedTemplate==null){
			NamedJdbcTemplate t = new JFishNamedJdbcTemplate(this);
			/*if(logJdbcSql){
				AspectJProxyFactory ajf = new AspectJProxyFactory(t);
				ajf.setProxyTargetClass(true);
				ajf.addAspect(JFishJdbcTemplateProxy.class);
				t = ajf.getProxy();
			}*/
			this.namedTemplate = t;
		}
	}
 
	/*
	public int update(String sql, Object[] args, final KeyHolder generatedKeyHolder) throws DataAccessException {
		ArgsPreparedStatementCreator psc = new ArgsPreparedStatementCreator(sql, args);
		return this.update(psc, generatedKeyHolder);
	}*/


	@Override
	public int updateWithKeyHolder(final SimpleArgsPreparedStatementCreator spsc, final KeyHolder generatedKeyHolder) throws DataAccessException {
		return updateWith(spsc, new AroundPreparedStatementExecute() {
			
			@Override
			public void afterExecute(PreparedStatement ps, int rows) throws SQLException {
				if(generatedKeyHolder==null)
					return ;
				List<Map<String, Object>> generatedKeys = generatedKeyHolder.getKeyList();
				generatedKeys.clear();
				ResultSet keys = ps.getGeneratedKeys();
				if (keys != null) {
					try {
						RowMapperResultSetExtractor<Map<String, Object>> rse = new RowMapperResultSetExtractor<Map<String, Object>>(getColumnMapRowMapper(), 1);
						generatedKeys.addAll(rse.extractData(keys));
					}
					finally {
						JdbcUtils.closeResultSet(keys);
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("SQL update affected " + rows + " rows and returned " + generatedKeys.size() + " keys");
				}
				
			}
		});
	}

	@Override
	public int updateWith(final SimpleArgsPreparedStatementCreator spsc) throws DataAccessException {
		final PreparedStatementSetter pss = this.newArgPreparedStatementSetter(spsc.getArgs());
		return update(spsc, pss);
	}
	
	@Override
	public int updateWith(final SimpleArgsPreparedStatementCreator spsc, final AroundPreparedStatementExecute action) throws DataAccessException {
		final PreparedStatementSetter pss = this.newArgPreparedStatementSetter(spsc.getArgs());
		return execute(spsc, new PreparedStatementCallback<Integer>() {
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException {
				try {
					if(action!=null)
						action.beforeExecute(pss, ps);
					else
						pss.setValues(ps);
					
					int rows = ps.executeUpdate();
					if (logger.isDebugEnabled()) {
						logger.debug("SQL update affected " + rows + " rows");
					}
					
					if(action!=null)
						action.afterExecute(ps, rows);
					
					return rows;
				}
				finally {
					if (pss instanceof ParameterDisposer) {
						((ParameterDisposer) pss).cleanupParameters();
					}
				}
			}
		});
	}
	
	@Override
	public int updateWith(String sql, Object[] args, final AroundPreparedStatementExecute action) throws DataAccessException {
		SimpleArgsPreparedStatementCreator psc = new SimpleArgsPreparedStatementCreator(sql, args);
		return updateWith(psc, action);
	}
	

	public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		return super.query(sql, args, rowMapper);
	}
	
	public int update(String sql, Object... args) throws DataAccessException {
		return super.update(sql, args);
	}
	

	public NamedJdbcTemplate getNamedTemplate() {
		return namedTemplate;
	}

	public void setNamedTemplate(NamedJdbcTemplate namedTemplate) {
		this.namedTemplate = namedTemplate;
	}

	protected Connection getConnection(){
		Connection con = DataSourceUtils.getConnection(getDataSource());
		return con;
	}

	protected void closeConnection(Connection con){
		DataSourceUtils.releaseConnection(con, getDataSource());
	}
	
	public <T> T query(PreparedStatementCreator psc, final PreparedStatementSetter pss, final ResultSetExtractor<T> rse) throws DataAccessException {

		Assert.notNull(rse, "ResultSetExtractor must not be null");
		logger.debug("Executing prepared SQL query");

		return execute(psc, new PreparedStatementCallback<T>() {
			public T doInPreparedStatement(PreparedStatement ps) throws SQLException {
				ResultSet rs = null;
				try {
					if (pss != null) {
						pss.setValues(ps);
					}
					long queryStart = System.currentTimeMillis();
					rs = ps.executeQuery();
					long quyeryEnd = System.currentTimeMillis();
					
					ResultSet rsToUse = rs;
					if (getNativeJdbcExtractor() != null) {
						rsToUse = getNativeJdbcExtractor().getNativeResultSet(rs);
					}
					
					T result = rse.extractData(rsToUse);
					
					if(isDebug()){
						logger.info("===>>> executeQuery cost time (milliseconds): " + (quyeryEnd-queryStart));
						long costTime = System.currentTimeMillis()-quyeryEnd;
						logger.info("===>>> extractData cost time (milliseconds): " + costTime);
					}
					
					return result;
				}
				finally {
					JdbcUtils.closeResultSet(rs);
					if (pss instanceof ParameterDisposer) {
						((ParameterDisposer) pss).cleanupParameters();
					}
				}
			}
		});
	}
	
	protected PreparedStatementSetter newArgPreparedStatementSetter(Object[] args) {
		return new DbmArgumentPreparedStatementSetter(args);
	}
	
}
