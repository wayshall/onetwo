package org.onetwo.plugins.task.entity;

import org.onetwo.common.utils.StringUtils;

public class TaskConfig {
	
	private int tryTimes = 3;
	private int tryIntervalInSeconds = 60;
	private String attachmentDir;
	

	public String getAttachmentPath(String path){
		String realpath = getAttachmentDir() + StringUtils.appendStartWith(path, "/");
		return realpath;
	}
	
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
	public String getAttachmentDir() {
		return attachmentDir;
	}
	public void setAttachmentDir(String attachmentDir) {
		this.attachmentDir = attachmentDir;
	}
	
}
