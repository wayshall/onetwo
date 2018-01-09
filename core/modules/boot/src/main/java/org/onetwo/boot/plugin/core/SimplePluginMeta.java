package org.onetwo.boot.plugin.core;


public class SimplePluginMeta implements PluginMeta {
	
	private final String name;
	private final String version;
	
	public SimplePluginMeta(String name, String version) {
		super();
		this.name = name;
		this.version = version;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "PluginMeta [name=" + name + ", version=" + version + "]";
	}
	
	
}
