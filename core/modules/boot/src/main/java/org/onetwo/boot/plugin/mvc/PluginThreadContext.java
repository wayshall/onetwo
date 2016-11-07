package org.onetwo.boot.plugin.mvc;

import org.onetwo.boot.plugin.core.WebPlugin;
import org.springframework.web.method.HandlerMethod;

public class PluginThreadContext {
	
	private final WebPlugin plugin;
	private final HandlerMethod handler;
	
	public PluginThreadContext(WebPlugin plugin, HandlerMethod handler) {
		super();
		this.plugin = plugin;
		this.handler = handler;
	}
	public WebPlugin getPlugin() {
		return plugin;
	}
	public HandlerMethod getHandler() {
		return handler;
	}
	
}
