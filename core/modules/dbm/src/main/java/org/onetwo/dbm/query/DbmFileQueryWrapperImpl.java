package org.onetwo.dbm.query;

import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.common.db.dquery.NamedQueryInvokeContext;
import org.onetwo.common.db.filequery.DefaultFileQueryWrapper;
import org.onetwo.common.db.filequery.DbmNamedQueryInfo;
import org.onetwo.common.db.filequery.spi.QueryProvideManager;
import org.onetwo.common.utils.Assert;

public class DbmFileQueryWrapperImpl extends DefaultFileQueryWrapper {

	private NamedQueryInvokeContext invokeContext;
	

	public DbmFileQueryWrapperImpl(QueryProvideManager queryProvideManager, DbmNamedQueryInfo info, boolean count, NamedQueryInvokeContext invokeContext) {
		super(queryProvideManager, info, count, invokeContext.getParser());
		Assert.notNull(queryProvideManager);
		this.invokeContext = invokeContext;
		
	}
	
	protected DbmQueryWrapper createDataQuery(String sql, Class<?> mappedClass){
		DbmQueryWrapper dataQuery = this.baseEntityManager.createSQLQuery(sql, mappedClass);
		if(!countQuery){
			dataQuery.setRowMapper(baseEntityManager.getRowMapperFactory().createRowMapper(invokeContext));
		}
		return dataQuery;
	}

	public NamedQueryInvokeContext getInvokeContext() {
		return invokeContext;
	}

}
