package org.onetwo.common.utils.propconf;

import java.util.Properties;

import org.junit.Test;

public class PropConfigTest {

	@Test
	public void testWebConfigLoad(){
		AppConfig s = AppConfig.create("siteConfig-base.properties");
		System.out.println("============================");
		s.getBoolean("test");
		Properties config = (Properties)s.getConfig();
	}
}
