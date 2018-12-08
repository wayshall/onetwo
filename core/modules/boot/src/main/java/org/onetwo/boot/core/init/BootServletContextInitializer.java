package org.onetwo.boot.core.init;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.onetwo.common.web.init.CommonWebFilterInitializer;
import org.springframework.boot.web.servlet.ServletContextInitializer;

/****
 * 注册自定义的filter
 * @author way
 *
 */
public class BootServletContextInitializer extends CommonWebFilterInitializer implements ServletContextInitializer {

	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
		this.onServletContextStartup(servletContext);
    }

	@Override
	protected void registeredMultipartFilter(ServletContext servletContext){
//		this.registeredMultipartFilter(servletContext, SpringMultipartFilterProxy.class);
	}

	/*protected void registeredEncodingFilter(ServletContext servletContext, Class<? extends Filter> encodingFilterClass){
		//boot会自动注册
	}*/
	protected void registeredHiddenMethodFilter(ServletContext servletContext, Class<? extends Filter> initFilterClass){
		//boot会自动注册
	}
	
	protected void registeredAjaxAnywhere(ServletContext servletContext, Class<? extends Filter> ajaxFilterClass){
		//不需要。。。。
	}
}
