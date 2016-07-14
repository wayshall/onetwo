package org.onetwo.boot.plugin.core;

abstract public class PluginAdapter implements Plugin {
	
	public String toString(){
		return this.getPluginMeta().toString();
	}
	
}
