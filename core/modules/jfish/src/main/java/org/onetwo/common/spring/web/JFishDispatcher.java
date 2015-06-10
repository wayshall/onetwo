package org.onetwo.common.spring.web;

import javax.servlet.ServletException;

import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;
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
	
	private JFishWebMvcPluginManager jfishPluginManager;
	
	public JFishDispatcher(){
		setDetectAllViewResolvers(false);
	}
	
	

	public JFishDispatcher(WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
		setDetectAllViewResolvers(false);
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() {

//		jfishPluginManager = SpringApplication.getInstance().getBean(JFishPluginManager.class);
		this.setContextClass(JFishMvcApplicationContext.class);
		
		WebApplicationContext appContext = super.initWebApplicationContext();
//				SpringApplication.initApplication(appContext);
		SpringApplication.getInstance().printBeanNames();
		jfishPluginManager = JFishPluginManagerFactory.getPluginManager();
		jfishPluginManager.onWebApplicationContextStartup(appContext);
		
		this.getServletContext().setAttribute(JFishWebMvcPluginManager.JFISH_PLUGIN_MANAGER_KEY, jfishPluginManager);

//		jfishPluginManager.onWebApplicationContextStartupCompleted(appContext);
		return appContext;
	}
	
	@Override
	protected void initFrameworkServlet() throws ServletException {
		super.initFrameworkServlet();
//		jfishPluginManager.onWebApplicationContextStartupCompleted(appContext);
	}
	

	@Override
	public void destroy() {
		jfishPluginManager.destroy(getWebApplicationContext());
		super.destroy();
	}
	
	
}
