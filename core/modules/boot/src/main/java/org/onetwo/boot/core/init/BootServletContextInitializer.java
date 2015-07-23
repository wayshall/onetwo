package org.onetwo.boot.core.init;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.onetwo.common.web.init.CommonWebFilterInitializer;
import org.springframework.boot.context.embedded.ServletContextInitializer;

public class BootServletContextInitializer extends CommonWebFilterInitializer implements ServletContextInitializer {

	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
		this.onServletContextStartup(servletContext);
    }

	protected void registeredHiddenMethodFilter(ServletContext servletContext, Class<? extends Filter> initFilterClass){
		//boot会自动注册
	}
}
