package org.onetwo.common.web;

import org.junit.Test;
import org.onetwo.common.utils.propconf.AppConfig;

public class BaseSiteConfigTest {
	
	static class SiteConfig extends AppConfig {
		protected SiteConfig() {
			super("siteConfig-base");
		}

	}

	@Test
	public void testConfigLoad(){
		SiteConfig config = new SiteConfig();
		System.out.println(config.getVariable("BaseException"));
	}
}
