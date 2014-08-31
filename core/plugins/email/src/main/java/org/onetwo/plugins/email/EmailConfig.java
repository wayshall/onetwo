package org.onetwo.plugins.email;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailConfig implements LoadableConfig {

	public static final String MAIL_SERVICE_CLASS_KEY = "mail.service";
	public static final String MAIL_SENDER_CLASS_KEY = "mail.sender";
	public static final String JAVA_MAIL_PROPERTIES_KEY = "javaMailProperties.";
	
	private JFishProperties config;

	public EmailConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
	}

	public Class<?> getMailServiceClass(){
		return config.getClass(MAIL_SENDER_CLASS_KEY, JavaMailServiceImpl.class);
	}

	public Class<?> getMailSender(){
		return config.getClass(MAIL_SENDER_CLASS_KEY, JavaMailSenderImpl.class);
	}

	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}

	/*public String getQueueNamePrefix(){
		return config.getProperty("queue.name.prefix", "queue-");
	}*/
	

}
