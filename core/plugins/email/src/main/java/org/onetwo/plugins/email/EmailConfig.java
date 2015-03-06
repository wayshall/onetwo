package org.onetwo.plugins.email;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class EmailConfig implements LoadableConfig {

//	public static final String MAIL_SENDER_ACTIVE = "mail.sender.active";
	
	public static final String MAIL_SERVICE_CLASS_KEY = "mail.service.class";
	//javamail, queue
	public static final String MAIL_SERVICE = "mail.service";
	public static final String MAIL_SERVICE_JAVAMAIL = "javamail";
//	public static final String MAIL_SENDER_QUEUE = "queue";
	public static final String JAVA_MAIL_PROPERTIES_KEY = "javaMailProperties.";
	

	public static final String MAIL_USERNAME_KEY = "username";
	public static final String ATTACHMENT_DIR_KEY = "attachment.dir";
	
	private JFishProperties config;

	public EmailConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
		/*if(!FileUtils.exists(getAttachmentDir())){
//			throw new BaseException("attachement dir is not exists: [" + getAttachmentDir() + "]");
		}*/
	}
	
	/*public boolean isMailSendActive(){
		return config.getBoolean(MAIL_SENDER_ACTIVE, true);
	}
*/
	public Class<?> getMailServiceClass(){
		return config.getClass(MAIL_SERVICE_CLASS_KEY, JavaMailServiceImpl.class);
	}
	
	/***
	 * 
	 * @return
	 */
	public boolean isDefaultJavamailService(){
		return MAIL_SERVICE_JAVAMAIL.equals(getMailService());
	}

	public String getMailService(){
		return config.getProperty(MAIL_SERVICE, MAIL_SERVICE_JAVAMAIL);
	}

	public String getUsername(){
		return config.getProperty(MAIL_USERNAME_KEY);
	}
	
	public String getAttachmentDir(){
		return config.getDir(ATTACHMENT_DIR_KEY, System.getProperty("java.io.tmpdir")+"/email-attachment/");
	}

	public String getEmailAttachmentPath(String path){
		String realpath = getAttachmentDir() + path;
		return realpath;
	}

	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}

	/*public String getQueueNamePrefix(){
		return config.getProperty("queue.name.prefix", "queue-");
	}*/
	

}
