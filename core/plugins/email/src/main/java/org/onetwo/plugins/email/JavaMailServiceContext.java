package org.onetwo.plugins.email;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.google.common.collect.Sets;

@Configuration
public class JavaMailServiceContext implements InitializingBean {
	
//	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private AppConfig appConfig;
	
	@Resource
	private EmailConfig emailConfig;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(appConfig, "appConfig must not be null!");
	}
	

	@Bean
	public JavaMailSender javaMailSender() throws IOException {
		Set<String> excludeKeys = Sets.newHashSet();
		excludeKeys.add(EmailConfig.MAIL_SERVICE_CLASS_KEY);
		excludeKeys.add(EmailConfig.JAVA_MAIL_PROPERTIES_KEY);

		/*Class<?> implClass = emailConfig.getMailSender();
		if(!JavaMailSenderImpl.class.isAssignableFrom(implClass))
			throw new ServiceException("java mail sender must a instance of " + JavaMailSenderImpl.class.getName());*/
		
//		JavaMailSenderImpl sender = (JavaMailSenderImpl)ReflectUtils.newInstance(implClass);
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		
		Properties javaMailProperties = new Properties();
		Enumeration<String> names = (Enumeration<String>) emailConfig.getSourceConfig().propertyNames();
		BeanWrapper bw = SpringUtils.newBeanWrapper(sender);
		while (names.hasMoreElements()) {
			String propertyName = names.nextElement();
			String value = emailConfig.getSourceConfig().getProperty(propertyName);
			if (propertyName.startsWith(EmailConfig.JAVA_MAIL_PROPERTIES_KEY)) {
				propertyName = propertyName.substring(EmailConfig.JAVA_MAIL_PROPERTIES_KEY.length());
//				LangUtils.println("mail config : ${0}, ${1}", propertyName, value);
				javaMailProperties.setProperty(propertyName, value);
			}else if(excludeKeys.contains(propertyName)){
				logger.info("ignore mail config: {}", propertyName);
			} else {
				if(bw.isWritableProperty(propertyName))
					bw.setPropertyValue(propertyName, value);
			}
		}
		sender.setJavaMailProperties(javaMailProperties);
		return sender;
	}


	@Bean
	public JavaMailService javaMailService() throws Exception{
		Class<?> implClass = emailConfig.getMailServiceClass();
		if(!JavaMailService.class.isAssignableFrom(implClass))
			throw new ServiceException("java mail sender must a instance of " + JavaMailService.class.getName());
		
		JavaMailService jms = (JavaMailService)ReflectUtils.newInstance(implClass);
		return jms;
	}

}
