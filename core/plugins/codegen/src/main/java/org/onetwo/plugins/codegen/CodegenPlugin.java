package org.onetwo.plugins.codegen;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class CodegenPlugin extends AbstractContextPlugin<CodegenPlugin> {

	private static CodegenPlugin instance;
	
	
	public static CodegenPlugin getInstance() {
		return instance;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(CodegenAppContext.class);
	}


	@Override
	public void setPluginInstance(CodegenPlugin plugin){
		instance = plugin;
	}


}
