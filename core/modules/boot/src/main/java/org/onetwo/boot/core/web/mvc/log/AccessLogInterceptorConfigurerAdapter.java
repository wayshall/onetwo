package org.onetwo.boot.core.web.mvc.log;

import org.onetwo.boot.core.web.mvc.interceptor.LoggerInterceptor;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * jfish and cloud extends
 * @author wayshall
 * <br/>
 */
public class AccessLogInterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {
	@Autowired
	private LoggerInterceptor loggerInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration reg = registry.addInterceptor(loggerInterceptor);
		if(LangUtils.isEmpty(loggerInterceptor.getPathPatterns())){
			return ;
		}
		reg.addPathPatterns(loggerInterceptor.getPathPatterns());
	}
}
