package org.onetwo.common.fish.spring.config;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.orm.DataBaseConfig;
import org.onetwo.common.fish.orm.DefaultDataBaseConfig;
import org.onetwo.common.fish.spring.JFishDaoImplementor;

abstract public class JFishOrmConfigurerAdapter implements JFishOrmConfigurator {
	
	private DataBaseConfig dataBaseConfig;

	public JFishOrmConfigurerAdapter() {
		this.dataBaseConfig = new DefaultDataBaseConfig();
	}


	@Override
	public boolean isLogJdbcSql() {
		return false;
	}

	@Override
	public boolean isWatchSqlFile() {
		return true;
	}

	@Override
	public DataBaseConfig getDataBaseConfig() {
		return dataBaseConfig;
	}

	@Override
	public JFishEntityManager jfishEntityManager(JFishDaoImplementor jfishDao) {
		return null;
	}

}
