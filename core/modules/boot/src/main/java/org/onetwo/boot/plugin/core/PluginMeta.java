package org.onetwo.boot.plugin.core;

import org.onetwo.common.utils.StringUtils;

public interface PluginMeta {
	String getName();
	String getVersion();
	default String getContextPath() {
		return StringUtils.appendStartWith(getName(), "/");
	}
}
