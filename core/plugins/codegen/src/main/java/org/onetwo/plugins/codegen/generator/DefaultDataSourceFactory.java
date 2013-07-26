package org.onetwo.plugins.codegen.generator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DefaultDataSourceFactory {
	
	private Map<Long, DataSource> datasourceCaches = new ConcurrentHashMap<Long, DataSource>();

	public DataSource create(DatabaseEntity database){
		DataSource ds = datasourceCaches.get(database.getId());
		if(ds!=null)
			return ds;
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setUser(database.getUsername());
		cpds.setPassword(database.getPassword());
		try {
			cpds.setDriverClass(database.getDriverClass());
			cpds.setJdbcUrl(database.getJdbcUrl());
		} catch (Exception e) {
			throw new ServiceException("create datasrouce error: " + e.getMessage());
		}
		datasourceCaches.put(database.getId(), cpds);
		return cpds;
	}
}
