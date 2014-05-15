package org.onetwo.common.spring.web;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/****
 * call onMvcContextClasses
 * @author weishao
 * 
 */
@SuppressWarnings("serial")
public class JFishDispatcher extends DispatcherServlet {
	
	private JFishPluginManager jfishPluginManager;

	protected WebApplicationContext initWebApplicationContext() {

//		jfishPluginManager = SpringApplication.getInstance().getBean(JFishPluginManager.class);
		this.setContextClass(JFishMvcApplicationContext.class);
		WebApplicationContext appContext = super.initWebApplicationContext();
//				SpringApplication.initApplication(appContext);
		SpringApplication.getInstance().printBeanNames();
		jfishPluginManager = JFishPluginManagerFactory.getPluginManager();
		jfishPluginManager.onInitWebApplicationContext(appContext);
		
		this.getServletContext().setAttribute(JFishPluginManager.JFISH_PLUGIN_MANAGER_KEY, jfishPluginManager);
		
		return appContext;
	}

	@Override
	public void destroy() {
		jfishPluginManager.destroy();
		super.destroy();
	}
	
	
}
