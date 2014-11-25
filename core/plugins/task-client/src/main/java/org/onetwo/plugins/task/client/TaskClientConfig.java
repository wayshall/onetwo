package org.onetwo.plugins.task.client;

import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.email.EmailConfig;
import org.onetwo.plugins.email.EmailPlugin;
import org.springframework.util.Assert;

public class TaskClientConfig extends AbstractLoadingConfig {
	public static final String ATTACHMENT_DIR = "email.attachment.dir";
	public static final String MAIL_SERVICE_TASK_CLIENT = "task-client";

	private String emailAttachmentDir;
	
	@Override
	public void initConfig(JFishProperties config) {
		emailAttachmentDir = config.getDir(ATTACHMENT_DIR, "");
//		String dir = EmailPlugin.getInstance().getConfig().getAttachmentDir();
		Assert.hasText(emailAttachmentDir, "email plugin has not config ["+ATTACHMENT_DIR+"]");
		
	}

	public String getEmailAttachmentDir() {
		return emailAttachmentDir;
	}
	
	/****
	 * 如果任务配置的邮件附件目录和邮件插件配置的目录不一致，则需要重新保存附件到任务的附件目录
	 * @return
	 */
	public boolean isRestoreEmailAttachment() {
		String emailDir = EmailPlugin.getInstance().getConfig().getAttachmentDir();
		return !getEmailAttachmentDir().equalsIgnoreCase(emailDir);
	}
	
	public boolean isTaskClientMailService(){
		EmailConfig econfig = EmailPlugin.getInstance().getConfig();
		return MAIL_SERVICE_TASK_CLIENT.equalsIgnoreCase(econfig.getMailService());
	}

}
