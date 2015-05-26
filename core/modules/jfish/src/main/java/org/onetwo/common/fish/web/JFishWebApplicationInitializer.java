package org.onetwo.common.fish.web;


/***
 * @see JFishSpringDispatcherServletInitializer#createRootApplicationContext()
 * @author Administrator
 *
 */
//@Order(JFishUtils.WEBAPP_INITIALIZER_ORDER)
public class JFishWebApplicationInitializer /*extends AbstractContextLoaderInitializer*/ {
	

	/*@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		if(app==null){
			registerContextLoaderListener(servletContext);
		}
		servletContext.addListener(IntrospectorCleanupListener.class);
	}
	
	@Override
	protected WebApplicationContext createRootApplicationContext() {
		WebApplicationContext webapp = new JFishWebApplicationContext();
		SpringApplication.initApplication(webapp);
		return webapp;
	}*/
	
	

}
