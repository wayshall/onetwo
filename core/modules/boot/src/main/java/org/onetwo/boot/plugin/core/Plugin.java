package org.onetwo.boot.plugin.core;

import org.onetwo.common.utils.StringUtils;

public interface Plugin {
	
	public PluginMeta getPluginMeta();
	
	default public boolean contains(Class<?> clazz){
		return clazz.getName().startsWith(getRootClass().getPackage().getName());
	}
	
	default Class<?> getRootClass(){
		return this.getClass();
	}

	default String getContextPath() {
		return StringUtils.appendStartWith(getPluginMeta().getName(), "/");
	}

	default String getTemplatePath() {
		return StringUtils.appendStartWith(getPluginMeta().getName(), "/");
	}
	
}
