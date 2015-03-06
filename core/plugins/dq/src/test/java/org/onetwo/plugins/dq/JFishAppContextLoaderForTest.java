package org.onetwo.plugins.dq;

import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;



public class JFishAppContextLoaderForTest extends SpringConfigApplicationContextLoader {
	

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{DqPluginContext.class};
	}

	
}
