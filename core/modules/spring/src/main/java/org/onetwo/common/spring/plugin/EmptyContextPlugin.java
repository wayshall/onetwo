package org.onetwo.common.spring.plugin;

import java.util.List;

public class EmptyContextPlugin implements ContextPlugin{
	
	EmptyContextPlugin(){
	}

	@Override
	public void init(ContextPluginMeta pluginMeta, String appEnv) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerEntityPackage(List<String> packages) {
		// TODO Auto-generated method stub
		
	}

}
