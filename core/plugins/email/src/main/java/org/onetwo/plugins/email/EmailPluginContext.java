package org.onetwo.plugins.email;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.google.common.collect.Sets;

@Configuration
public class EmailPluginContext implements InitializingBean {

	public static final String MAIL_SENDER_CLASS_KEY = "mail.sender";
	public static final String JAVA_MAIL_PROPERTIES_KEY = "javaMailProperties.";
	
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Resource
	private AppConfig appConfig;
	
	@Resource
	private Properties mailConfig;
	
	@Resource
	private freemarker.template.Configuration mailFreemarkerConfiguration;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(appConfig, "appConfig must not be null!");
	}

	@Bean
	public JavaMailService JavaMailService() throws Exception{
		JavaMailServiceImpl jm = new JavaMailServiceImpl();
		jm.setJavaMailSender(javaMailSender());
		jm.setConfiguration(mailFreemarkerConfiguration);
		return jm;
	}
	
	@Bean
	public JavaMailSender javaMailSender() throws IOException {
		Set<String> excludeKeys = Sets.newHashSet();
		excludeKeys.add(MAIL_SENDER_CLASS_KEY);

		String implClass = mailConfig.getProperty(MAIL_SENDER_CLASS_KEY);
		JavaMailSenderImpl sender = null;
		if(StringUtils.isNotBlank(implClass)){
			sender = ReflectUtils.newInstance(implClass);
			if(!JavaMailSenderImpl.class.isInstance(sender))
				throw new ServiceException("java mail sender must a instance of " + JavaMailSenderImpl.class.getName());
		}else{
			sender = new JavaMailSenderImpl();
		}
		Properties javaMailProperties = new Properties();
		Enumeration<String> names = (Enumeration<String>) mailConfig.propertyNames();
		BeanWrapper bw = SpringUtils.newBeanWrapper(sender);
		while (names.hasMoreElements()) {
			String propertyName = names.nextElement();
			String value = mailConfig.getProperty(propertyName);
			if (propertyName.startsWith(JAVA_MAIL_PROPERTIES_KEY)) {
				propertyName = propertyName.substring(JAVA_MAIL_PROPERTIES_KEY.length());
//				LangUtils.println("mail config : ${0}, ${1}", propertyName, value);
				javaMailProperties.setProperty(propertyName, value);
			}else if(excludeKeys.contains(propertyName)){
				logger.info("ignore mail config: {}", propertyName);
			} else {
				bw.setPropertyValue(propertyName, value);
			}
		}
		sender.setJavaMailProperties(javaMailProperties);
		return sender;
	}

	@Bean
	public PropertiesFactoryBean mailConfig() {
		String envLocation = "/email/mailconfig-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring("/email/mailconfig.properties", envLocation);
	}
	
	@Bean
	public FreeMarkerConfigurationFactoryBean mailFreemarkerConfiguration(){
		FreeMarkerConfigurationFactoryBean fcfb = new FreeMarkerConfigurationFactoryBean();
		fcfb.setTemplateLoaderPath("classpath:/email/ftl/");
		return fcfb;
	}

}
