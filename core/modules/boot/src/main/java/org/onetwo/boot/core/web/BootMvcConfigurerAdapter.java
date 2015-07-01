package org.onetwo.boot.core.web;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;

import org.onetwo.boot.core.config.JFishBootConfig;
import org.onetwo.boot.core.web.mvc.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.common.spring.converter.JFishStringToEnumConverterFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/****
 * 有关web mvc的一些扩展配置
 * @author way
 *
 */
public class BootMvcConfigurerAdapter extends WebMvcConfigurerAdapter implements InitializingBean {
	
//	@Autowired
	private BootWebExceptionResolver bootWebExceptionResolver;

	@Autowired
	private JFishBootConfig jfishBootConfig;
	
	
	@Override
    public void afterPropertiesSet() throws Exception {
//		Assert.notNull(bootWebExceptionResolver);
    }

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		Properties mediaTypes = jfishBootConfig.getMediaTypes();
		if (!CollectionUtils.isEmpty(mediaTypes)) {
			for (Entry<Object, Object> entry : mediaTypes.entrySet()) {
				String extension = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
				configurer.mediaType(extension, MediaType.valueOf((String) entry.getValue()));
			}
		}
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new BootFirstInterceptor());
	}

	@Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		if(bootWebExceptionResolver!=null){
			exceptionResolvers.add(bootWebExceptionResolver);
		}
    }

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new JFishStringToEnumConverterFactory());
	}

}
