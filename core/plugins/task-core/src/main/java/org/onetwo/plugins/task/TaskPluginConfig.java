package org.onetwo.plugins.task;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.task.entity.TaskConfig;
import org.springframework.util.Assert;

public class TaskPluginConfig implements LoadableConfig {
	
	public static final String ATTACHMENT_DIR = "email.attachment.dir";

	private JFishProperties config;
	
	private TaskConfig taskConfig;

	public TaskPluginConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
		
		taskConfig = new TaskConfig();
		taskConfig.setTryTimes(config.getInt("try.times", 3));
		

		String dir = config.getDir(ATTACHMENT_DIR, "");
//		String dir = EmailPlugin.getInstance().getConfig().getAttachmentDir();
		Assert.hasText(dir, "email plugin has not config ["+ATTACHMENT_DIR+"]");
		taskConfig.setEmailAttachmentDir(dir);
		
	}

	public TaskConfig getTaskConfig() {
		return taskConfig;
	}

	@Override
	public JFishProperties getSourceConfig() {
		if(config==null){
			throw new BaseException("config is not load!");
		}
		return config;
	}
	
	/*public String getQueueNamePrefix(){
		return config.getProperty("queue.name.prefix", "queue-");
	}*/
	

}
