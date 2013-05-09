package org.onetwo.common.fish.spring.config;


public interface JFishOrmConfigurator {

	public String[] getModelBasePackages();
	
	public boolean isLogJdbcSql();
	
	public boolean isWatchSqlFile();
}
