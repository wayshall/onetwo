package org.onetwo.plugins.task.entity;


public class TaskConfig {
	
	private int tryTimes = 3;
	private int tryIntervalInSeconds = 60;
//	private boolean restoreEmailAttachment;
	
	
	public int getTryTimes() {
		return tryTimes;
	}
	public void setTryTimes(int tryTimes) {
		this.tryTimes = tryTimes;
	}
	public int getTryIntervalInSeconds() {
		return tryIntervalInSeconds;
	}
	public void setTryIntervalInSeconds(int tryIntervalInSeconds) {
		this.tryIntervalInSeconds = tryIntervalInSeconds;
	}

	/****
	 * 如果任务配置的邮件附件目录和邮件插件配置的目录不一致，则需要重新保存附件到任务的附件目录
	 * @return
	 */
	/*public boolean isRestoreEmailAttachment() {
		String emailDir = EmailPlugin.getInstance().getConfig().getAttachmentDir();
		return !getEmailAttachmentDir().equalsIgnoreCase(emailDir);
	}*/
	
}
