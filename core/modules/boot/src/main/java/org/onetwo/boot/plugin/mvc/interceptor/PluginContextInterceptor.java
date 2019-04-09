package org.onetwo.boot.plugin.mvc.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.WebInterceptorAdapter;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.boot.plugin.mvc.PluginContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

public class PluginContextInterceptor extends WebInterceptorAdapter {

	
	@Autowired
	private PluginManager pluginManager;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = this.getHandlerMethod(handler);
		if (handler==null) {
			return true;
		}
		Optional<WebPlugin> webPlugin = this.pluginManager.findPluginByElementClass(handlerMethod.getBeanType());
		if(webPlugin.isPresent()){
			PluginContextHolder.set(webPlugin, handlerMethod);
		}
		return true;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		PluginContextHolder.reset();
	}

	@Override
	public int getOrder() {
		return LAST;
	}

}
