package org.onetwo.common.jfish;


import org.onetwo.common.CommonPackageRoot;
import org.onetwo.common.test.spring.JFishAppContextTestLoader;

public class JFishAppContextLoaderForTest extends JFishAppContextTestLoader {
	

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{CommonPackageRoot.class};
	}

	
}
