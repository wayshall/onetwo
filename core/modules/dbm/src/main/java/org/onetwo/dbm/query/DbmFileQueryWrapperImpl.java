package org.onetwo.dbm.query;

import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.common.db.dquery.NamedQueryInvokeContext;
import org.onetwo.common.db.filequery.DefaultFileQueryWrapper;
import org.onetwo.common.db.filequery.DbmNamedFileQueryInfo;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.utils.Assert;

public class DbmFileQueryWrapperImpl extends DefaultFileQueryWrapper {

//	private JFishNamedFileQueryInfo info;
//	private QueryProvideManager baseEntityManager;
	private NamedQueryInvokeContext invokeContext;
	

	public DbmFileQueryWrapperImpl(QueryProvideManager queryProvideManager, DbmNamedFileQueryInfo info, boolean count, NamedQueryInvokeContext invokeContext) {
		super(queryProvideManager, info, count, invokeContext.getParser());
		Assert.notNull(queryProvideManager);
		this.invokeContext = invokeContext;
		
	}
	
	protected DbmQueryWrapper createDataQuery(String sql, Class<?> mappedClass){
		DbmQueryWrapper dataQuery = this.baseEntityManager.createSQLQuery(sql, mappedClass);
		dataQuery.setRowMapper(baseEntityManager.getRowMapperFactory().createRowMapper(invokeContext));
		return dataQuery;
	}

	public NamedQueryInvokeContext getInvokeContext() {
		return invokeContext;
	}

}
