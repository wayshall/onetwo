package org.onetwo.common.jdbc;


import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

/****
 * 基于jdbc的数据访问类
 * @author weishao
 *
 */
public abstract class JdbcDaoSupport extends DaoSupport {

	protected JFishJdbcOperations jdbcTemplate;
	protected NamedJdbcTemplate namedParameterJdbcTemplate;

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

	@Override
	protected void initDao() throws Exception {
		super.initDao();
	}

	protected void initTemplateConfig() {
		if(this.namedParameterJdbcTemplate==null)
			this.namedParameterJdbcTemplate = new JFishNamedJdbcTemplate(getJdbcTemplate());
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

}

