package org.onetwo.common.utils.propconf;

import org.onetwo.common.web.utils.WebVariableSupporterProvider;

public class TestConfig extends PropConfig {

	public TestConfig(){
		super("test1.properties", (VariableExpositor)null);
		expositor = new VariableExpositor(new WebVariableSupporterProvider(), false);
		this.initAppConfig(true);
	}

}
