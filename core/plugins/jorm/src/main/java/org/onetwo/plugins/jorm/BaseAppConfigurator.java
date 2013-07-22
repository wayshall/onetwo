package org.onetwo.plugins.jorm;

import org.onetwo.common.fish.orm.DataBaseConfig;
import org.onetwo.common.fish.orm.DefaultDataBaseConfig;
import org.onetwo.common.fish.spring.config.JFishAppConfigrator;
import org.onetwo.common.fish.spring.config.JFishOrmConfigurator;
import org.onetwo.common.web.config.BaseSiteConfig;


abstract public class BaseAppConfigurator implements JFishAppConfigrator, JFishOrmConfigurator {

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
	public String[] getModelBasePackages() {
		return new String[]{getJFishBasePackage()};
	}


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
