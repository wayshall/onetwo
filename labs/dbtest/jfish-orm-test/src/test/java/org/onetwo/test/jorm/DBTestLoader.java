package org.onetwo.test.jorm;

import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;
import org.onetwo.test.jorm.JFishOrmConfig;
import org.onetwo.test.jorm.JFishOrmContextConfig;

public class DBTestLoader extends SpringConfigApplicationContextLoader {

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{JFishOrmContextConfig.class};
	}

	@Override
	protected String getAppEnvironment() {
		return JFishOrmConfig.getInstance().getAppEnvironment();
	}

}
