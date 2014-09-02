package org.onetwo.plugins.email;

import javax.annotation.Resource;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaMailServiceContext implements InitializingBean {
	
//	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Resource
	private AppConfig appConfig;
	
	@Resource
	private EmailConfig mailConfig;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(appConfig, "appConfig must not be null!");
	}

	@Bean
	public JavaMailService JavaMailService() throws Exception{
		Class<?> implClass = mailConfig.getMailServiceClass();
		if(!JavaMailService.class.isAssignableFrom(implClass))
			throw new ServiceException("java mail sender must a instance of " + JavaMailService.class.getName());
		
		JavaMailService jms = (JavaMailService)ReflectUtils.newInstance(implClass);
		return jms;
	}

}
