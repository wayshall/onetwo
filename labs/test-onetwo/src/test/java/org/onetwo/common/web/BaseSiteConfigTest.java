package org.onetwo.common.web;

import org.junit.Test;
import org.onetwo.common.web.config.BaseSiteConfig;

public class BaseSiteConfigTest {
	
	static class SiteConfig extends BaseSiteConfig {
		protected SiteConfig() {
			super("E:/mydev/test/config%20dir/siteConfig-base.properties");
		}

	}

	@Test
	public void testConfigLoad(){
		SiteConfig config = new SiteConfig();
		System.out.println(config.isDev());
	}
}
