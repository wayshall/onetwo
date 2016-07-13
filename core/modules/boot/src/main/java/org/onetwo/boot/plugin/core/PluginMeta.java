package org.onetwo.boot.plugin.core;

public interface PluginMeta {
	String getName();
	String getVersion();
	default String getContextPath() {
		return getName();
	}
}
