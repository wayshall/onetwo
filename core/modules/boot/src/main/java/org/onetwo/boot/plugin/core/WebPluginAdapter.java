package org.onetwo.boot.plugin.core;

abstract public class WebPluginAdapter implements WebPlugin {
	
	public String toString(){
		return this.getPluginMeta().toString();
	}
	
}
