package org.onetwo.common.jfishdbm.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class SpringDatasourceExecutor {

	@FunctionalInterface
	public static interface ExecutorCallback<R> {
	    R apply(Connection conn) throws Exception;
	}
	
	private DataSource dataSource;
	
	public SpringDatasourceExecutor(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public <T> T doInConnection(ExecutorCallback<T> func){
		Connection dbcon = null;
		try {
			dbcon = DataSourceUtils.getConnection(dataSource);
			return func.apply(dbcon);
		} catch (Exception e) {
			throw LangUtils.asBaseException("doInConnection error : " + e.getMessage() , e);
		} finally{
			DataSourceUtils.releaseConnection(dbcon, dataSource);
		}
	}
}
