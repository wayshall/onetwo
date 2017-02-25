package org.onetwo.dbm.query;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.dquery.NamedQueryInvokeContext;
import org.onetwo.common.db.filequery.DefaultFileQueryImpl;
import org.onetwo.common.db.filequery.JFishNamedFileQueryInfo;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.utils.Assert;
import org.springframework.jdbc.core.RowMapper;

public class JFishFileQueryImpl extends DefaultFileQueryImpl {

//	private JFishNamedFileQueryInfo info;
//	private QueryProvideManager baseEntityManager;
	private NamedQueryInvokeContext invokeContext;
	

	public JFishFileQueryImpl(QueryProvideManager queryProvideManager, JFishNamedFileQueryInfo info, boolean count, NamedQueryInvokeContext invokeContext) {
		super(queryProvideManager, info, count, invokeContext.getParser());
		Assert.notNull(queryProvideManager);
		this.invokeContext = invokeContext;
		
	}
	
	protected DataQuery createDataQuery(String sql, Class<?> mappedClass){
		DataQuery dataQuery = this.baseEntityManager.createSQLQuery(sql, mappedClass);
		setRowMapper(baseEntityManager.getRowMapperFactory().createRowMapper(invokeContext));
		return dataQuery;
	}


	public void setRowMapper(RowMapper<?> rowMapper) {
		this.getRawQuery(JFishDataQuery.class).getJfishQuery().setRowMapper(rowMapper);
	}

	public NamedQueryInvokeContext getInvokeContext() {
		return invokeContext;
	}

}
