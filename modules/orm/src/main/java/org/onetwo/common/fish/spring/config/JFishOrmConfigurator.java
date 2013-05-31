package org.onetwo.common.fish.spring.config;

import org.onetwo.common.fish.orm.DataBaseConfig;


public interface JFishOrmConfigurator {

	public String[] getModelBasePackages();
	
	public boolean isLogJdbcSql();
	
	public boolean isWatchSqlFile();
	
	public DataBaseConfig getDataBaseConfig();
}
