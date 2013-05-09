package org.onetwo.common.utils.propconf;

import org.junit.Test;

public class PropConfigTest {

	@Test
	public void testWebConfigLoad(){
		AppConfig s = AppConfig.create("siteConfig-base.properties");
		System.out.println("============================");
		s.getBoolean("test");
	}
}
