package org.onetwo.plugins.codegen.generator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultTableManagerFactory {
	
	private Map<Long, DefaultTableManager> tableManagerCaches = new ConcurrentHashMap<Long, DefaultTableManager>();;
	
	@Autowired
	private DefaultDataSourceFactory dataSourceFactory;

	@Autowired
	private TableComponentFacotry tableComponentFacotry;
	
	public DefaultTableManager createTableManager(DatabaseEntity db){
		DefaultTableManager tm = tableManagerCaches.get(db.getId());
		if(tm!=null)
			return tm;
		DataSource ds = dataSourceFactory.create(db);
		tm = new DefaultTableManager();
		tm.setDataSource(ds);
		tm.setDataBase(db.getDataBase());
		tm.setTableComponentFacotry(tableComponentFacotry);
		tableManagerCaches.put(db.getId(), tm);
		return tm;
	}

	public void setDataSourceFactory(DefaultDataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}

	public void setTableComponentFacotry(TableComponentFacotry tableComponentFacotry) {
		this.tableComponentFacotry = tableComponentFacotry;
	}
	
}
