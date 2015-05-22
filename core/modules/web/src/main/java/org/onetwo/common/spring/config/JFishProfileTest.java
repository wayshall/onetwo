package org.onetwo.common.spring.config;

import org.junit.Assert;
import org.junit.Test;

public class JFishProfileTest {
	
	@Test
	public void testPath(){
		String path = JFishProfiles.APP_CONFIG_PATH;
		Assert.assertEquals("webconf/application.properties", path);
		
		path = JFishProfiles.getEnvProperties("dev");
		Assert.assertEquals("webconf/application-dev.properties", path);
	}

}
