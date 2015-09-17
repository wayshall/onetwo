package org.onetwo.common.jfishdbm.jdbc;


import java.sql.Connection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.onetwo.common.jfishdbm.jdbc.mapper.JdbcDaoRowMapperFactory;
import org.onetwo.common.jfishdbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.common.log.JFishLoggerFactory;
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
	
	protected JFishJdbcOperations jdbcTemplate;
	protected NamedJdbcTemplate namedParameterJdbcTemplate;
	
	private RowMapperFactory rowMapperFactory;
	

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
	
	public <T> List<T> query(String sql, Class<T> entityClass, Object... args){
		return this.jdbcTemplate.query(sql, getDefaultRowMapper(entityClass), args);
	}

	protected <T> RowMapper<T> getDefaultRowMapper(Class<T> type){
		return (RowMapper<T>)this.rowMapperFactory.createDefaultRowMapper(type);
	}


//	@Resource(name="dataSource")
	final public void setDataSource(DataSource dataSource) {
		if (this.jdbcTemplate == null || dataSource != this.jdbcTemplate.getDataSource()) {
			this.jdbcTemplate = createJdbcTemplate(dataSource);
			initTemplateConfig();
		}
	}

	protected JFishJdbcOperations createJdbcTemplate(DataSource dataSource) {
		return new JFishJdbcTemplate(dataSource);
	}

	public final DataSource getDataSource() {
		return (this.jdbcTemplate != null ? this.jdbcTemplate.getDataSource() : null);
	}
	
	public final void setJdbcTemplate(JFishJdbcOperations jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		initTemplateConfig();
	}

	public final JFishJdbcOperations getJdbcTemplate() {
	  return this.jdbcTemplate;
	}

	protected void initTemplateConfig() {
		if(this.namedParameterJdbcTemplate==null){
			this.namedParameterJdbcTemplate = new JFishNamedJdbcTemplate(getJdbcTemplate());
		}
	}

	public NamedJdbcTemplate getNamedParameterJdbcTemplate() {
	  return namedParameterJdbcTemplate;
	}

	protected void checkDaoConfig() {
		if (this.jdbcTemplate == null) {
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

	public void setNamedParameterJdbcTemplate(NamedJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public RowMapperFactory getRowMapperFactory() {
		return rowMapperFactory;
	}

	public void setRowMapperFactory(RowMapperFactory rowMapperFactory) {
		this.rowMapperFactory = rowMapperFactory;
	}

}

