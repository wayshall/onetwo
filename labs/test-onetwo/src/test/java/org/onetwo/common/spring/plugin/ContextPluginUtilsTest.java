package org.onetwo.common.spring.plugin;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class ContextPluginUtilsTest {
	
	@Test
	public void testPath(){
		String propfile = ContextPluginUtils.getConfigPath("/plugins/activemq/", "activemq-config");
		System.out.println("p: " + propfile);
		Assert.assertEquals("/plugins/activemq/activemq-config.properties", propfile);
		
		String[] propfiles = ContextPluginUtils.getEnvConfigPaths("activemq", "dev");
		LangUtils.println("pfiles: ${0}, ${1}", propfiles);
		Assert.assertEquals("/plugins/activemq/activemq-config.properties", propfiles[0]);
		Assert.assertEquals("/plugins/activemq/activemq-config-dev.properties", propfiles[1]);
	}

}
