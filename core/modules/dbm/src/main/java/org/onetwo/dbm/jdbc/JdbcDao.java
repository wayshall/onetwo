package org.onetwo.dbm.jdbc;


import java.sql.Connection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.dbm.jdbc.mapper.JdbcDaoRowMapperFactory;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;

/****
 * 基于jdbc的数据访问类
 * @author weishao
 *
 */
public class JdbcDao {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected DbmJdbcOperations dbmJdbcOperations;
//	protected NamedJdbcTemplate namedParameterJdbcTemplate;
	
	private RowMapperFactory rowMapperFactory;
	protected DataSource dataSource;
	

	@PostConstruct
	public void initialize() {
		// Let abstract subclasses check their configuration.
		checkDaoConfig();

		// Let concrete implementations initialize themselves.
		try {
			initDao();
		}
		catch (Exception ex) {
			throw new BeanInitializationException("Initialization of DAO failed", ex);
		}
	}

	protected void initDao() throws Exception {
		if(this.rowMapperFactory==null){
			this.rowMapperFactory = new JdbcDaoRowMapperFactory();
		}
	}
	
	/*public <T> List<T> query(String sql, Class<T> entityClass, Object... args){
		return this.dbmJdbcOperations.query(sql, getRowMapper(entityClass), args);
	}*/

	protected <T> RowMapper<T> getRowMapper(Class<T> type){
		return (RowMapper<T>)this.rowMapperFactory.createRowMapper(type);
	}


//	@Resource(name="dataSource")
	final public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		if (this.dbmJdbcOperations == null || dataSource != this.dbmJdbcOperations.getDataSource()) {
			this.dbmJdbcOperations = createDbmJdbcOperations(dataSource);
//			initTemplateConfig();
		}
	}

	protected DbmJdbcOperations createDbmJdbcOperations(DataSource dataSource) {
		return new DbmJdbcTemplate(dataSource);
	}

	public final DataSource getDataSource() {
		return dataSource;
	}
	
	public final void setDbmJdbcOperations(DbmJdbcOperations jdbcTemplate) {
		this.dbmJdbcOperations = jdbcTemplate;
//		initTemplateConfig();
	}

	public final DbmJdbcOperations getDbmJdbcOperations() {
	  return this.dbmJdbcOperations;
	}

	/*protected void initTemplateConfig() {
		if(this.namedParameterJdbcTemplate==null){
			this.namedParameterJdbcTemplate = new DbmNamedJdbcTemplate(dbmJdbcOperations);
		}
	}
	protected NamedJdbcTemplate createNamedJdbcTemplate(DataSource dataSource) {
		return new DbmNamedJdbcTemplate(getJdbcTemplate());
	}

	public NamedJdbcTemplate getNamedParameterJdbcTemplate() {
	  return namedParameterJdbcTemplate;
	}*/

	protected void checkDaoConfig() {
		if (this.dbmJdbcOperations == null) {
			throw new IllegalArgumentException("'dataSource' or 'jdbcTemplate' is required");
		}
	}

	/*protected final SQLExceptionTranslator getExceptionTranslator() {
		return getJdbcTemplate().getExceptionTranslator();
	}*/

	protected final Connection getConnection() throws CannotGetJdbcConnectionException {
		return DataSourceUtils.getConnection(getDataSource());
	}
	
	protected final void releaseConnection(Connection con) {
		DataSourceUtils.releaseConnection(con, getDataSource());
	}

	/*public void setNamedParameterJdbcTemplate(NamedJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}*/

	public RowMapperFactory getRowMapperFactory() {
		return rowMapperFactory;
	}

	public void setRowMapperFactory(RowMapperFactory rowMapperFactory) {
		this.rowMapperFactory = rowMapperFactory;
	}

}

