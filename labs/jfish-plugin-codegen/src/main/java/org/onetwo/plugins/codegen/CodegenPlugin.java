package org.onetwo.plugins.codegen;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class CodegenPlugin extends AbstractJFishPlugin<CodegenPlugin> {

	private static CodegenPlugin instance;
	
	
	public static CodegenPlugin getInstance() {
		return instance;
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(CodegenContext.class);
	}
	
	public void setPluginInstance(CodegenPlugin plugin){
		instance = plugin;
	}

}
