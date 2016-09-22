package org.onetwo.dbm.dialet;

import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import org.onetwo.dbm.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.dbm.jdbc.SpringDatasourceExecutor;

public class DbmetaFetcher {
	
	public static DbmetaFetcher create(DataSource ds){
		return new DbmetaFetcher(ds);
	}

	private SpringDatasourceExecutor executor;
	

	public DbmetaFetcher(DataSource dataSource) {
		this.executor = new SpringDatasourceExecutor(dataSource);
	}
	
	public DBMeta getDBMeta(){
		return executor.doInConnection(dbcon->{
			DatabaseMetaData meta = dbcon.getMetaData();
			DBMeta dbmeta = new DBMeta(meta.getDatabaseProductName());
			dbmeta.setVersion(meta.getDatabaseProductVersion());
			return dbmeta;
		});
	}
}
