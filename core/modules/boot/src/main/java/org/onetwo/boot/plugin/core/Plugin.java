package org.onetwo.boot.plugin.core;

public interface Plugin {
	
	public PluginMeta getPluginMeta();
	
	default public boolean contains(Class<?> clazz){
		return clazz.getName().startsWith(getRootClass().getPackage().getName());
	}
	
	default Class<?> getRootClass(){
		return this.getClass();
	}
	
}
