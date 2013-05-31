package org.onetwo.common.jfish;

import org.onetwo.common.fish.spring.config.AbstractJFishOrmConfigurator;

public class TestOrmConfigurator extends AbstractJFishOrmConfigurator {

	@Override
	public String[] getModelBasePackages() {
		return null;
	}
	
	@Override
	public boolean isLogJdbcSql() {
		return false;
	}

	@Override
	public boolean isWatchSqlFile() {
		return false;
	}

}
