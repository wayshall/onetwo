package org.onetwo.boot.plugin.mvc;

import java.util.Optional;

import org.onetwo.boot.plugin.core.WebPlugin;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;

public class PluginContextHolder {

	private static final ThreadLocal<PluginThreadContext> pluginContextHolder = new NamedThreadLocal<>("plugin context");
	
	public static void set(Optional<WebPlugin> webPlugin, HandlerMethod handlerMethod){
		if(webPlugin.isPresent()){
			PluginThreadContext context = new PluginThreadContext(webPlugin.get(), handlerMethod);
			pluginContextHolder.set(context);
		}else{
			reset();
		}
	}
	
	public static Optional<PluginThreadContext> get(){
		return Optional.ofNullable(pluginContextHolder.get());
	}

	public static void reset(){
		pluginContextHolder.remove();
	}


}
