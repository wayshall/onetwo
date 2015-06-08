package appweb;


import org.onetwo.common.test.spring.JFishAppContextTestLoader;

public class JFishAppContextLoaderForTest extends JFishAppContextTestLoader {
	

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{CommonPackageRoot.class};
	}

	
}
