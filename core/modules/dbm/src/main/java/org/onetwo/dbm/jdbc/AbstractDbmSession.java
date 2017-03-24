package org.onetwo.dbm.jdbc;


import java.sql.Connection;

import javax.sql.DataSource;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.dbm.core.spi.DbmSessionImplementor;
import org.slf4j.Logger;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

/****
 * 基于jdbc的数据访问类
 * @author weishao
 *
 */
abstract public class AbstractDbmSession implements DbmSessionImplementor {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected DbmJdbcOperations dbmJdbcOperations;
	
	protected DataSource dataSource;
	private boolean debug;

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

//	@Resource(name="dataSource")
	final public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		/*if (this.dbmJdbcOperations == null || dataSource != this.dbmJdbcOperations.getDataSource()) {
			this.dbmJdbcOperations = createDbmJdbcOperations(dataSource);
//			initTemplateConfig();
		}*/
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

}

