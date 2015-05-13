package org.onetwo.common.jfishdbm;

import org.onetwo.common.jfishdbm.JFishOrmTestConfig;
import org.onetwo.common.jfishdbm.JFishOrmTestContextConfig;
import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;

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
