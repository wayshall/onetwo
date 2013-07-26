package org.onetwo.common.fish.spring.config;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.orm.DataBaseConfig;
import org.onetwo.common.fish.spring.JFishDaoImplementor;


public interface JFishOrmConfigurator {

	public String[] getModelBasePackages();
	
	public boolean isLogJdbcSql();
	
	public boolean isWatchSqlFile();
	
	public DataBaseConfig getDataBaseConfig();
	
	public JFishEntityManager jfishEntityManager(JFishDaoImplementor jfishDao);
}
