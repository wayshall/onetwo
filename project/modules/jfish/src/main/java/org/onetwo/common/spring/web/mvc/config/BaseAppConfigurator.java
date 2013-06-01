package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.fish.orm.DataBaseConfig;
import org.onetwo.common.fish.orm.DefaultDataBaseConfig;
import org.onetwo.common.fish.spring.config.JFishAppConfigrator;
import org.onetwo.common.web.config.BaseSiteConfig;


abstract public class BaseAppConfigurator implements JFishAppConfigrator {

	private DataBaseConfig dataBaseConfig;
	
	public BaseAppConfigurator(){
		this.dataBaseConfig = new DefaultDataBaseConfig();
	}

	@Override
	public String[] getXmlBasePackages() {
		return getModelBasePackages();
	}

	/*@Override
	public String[] getModelBasePackages() {
		return null;
	}*/

	@Override
	public boolean isLogJdbcSql() {
		return BaseSiteConfig.getInstance().isJdbcSqlLog();
	}

	@Override
	public boolean isWatchSqlFile() {
		return BaseSiteConfig.getInstance().isJdbcSqlLog();
	}

	@Override
	public DataBaseConfig getDataBaseConfig() {
		return dataBaseConfig;
	}

}
