package org.onetwo.test.jorm;

import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;
import org.onetwo.test.jorm.JFishOrmTestConfig;
import org.onetwo.test.jorm.JFishOrmTestContextConfig;

public class DBTestLoader extends SpringConfigApplicationContextLoader {

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{JFishOrmTestContextConfig.class};
	}

	@Override
	protected String getAppEnvironment() {
		return JFishOrmTestConfig.getInstance().getAppEnvironment();
	}

}
