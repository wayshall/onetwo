package org.onetwo.plugins.codegen;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class CodegenWebPlugin extends AbstractJFishPlugin<CodegenWebPlugin> {

	private static CodegenWebPlugin instance;
	
	
	public static CodegenWebPlugin getInstance() {
		return instance;
	}


	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(CodegenContext.class);
	}
	
	public void setPluginInstance(CodegenWebPlugin plugin){
		instance = (CodegenWebPlugin)plugin;
	}

	@Override
	public boolean registerMvcResources() {
		return true;
	}

}
