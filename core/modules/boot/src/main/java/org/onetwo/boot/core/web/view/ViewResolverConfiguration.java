package org.onetwo.boot.core.web.view;

import java.util.List;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

/**
 * 协作视图
 * @author wayshall
 * <br/>
 */
@Configuration
public class ViewResolverConfiguration {
	@Bean
	@Autowired
	public ViewResolver viewResolver(ApplicationContext applicationContext, ContentNegotiationManager contentNegotiationManager) {
		List<View> views = SpringUtils.getBeans(applicationContext, View.class);
		ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
		viewResolver.setUseNotAcceptableStatusCode(true);
		viewResolver.setOrder(0);
		viewResolver.setDefaultViews(views);
//		List<View> views = LangUtils.asListWithType(View.class, xmlView(), jsonView());
//		viewResolver.setMediaTypes(mediaType());
//		viewResolver.setDefaultContentType(MediaType.TEXT_HTML);
//		viewResolver.setIgnoreAcceptHeader(true);
		viewResolver.setContentNegotiationManager(contentNegotiationManager);
		return viewResolver;
	}
}
