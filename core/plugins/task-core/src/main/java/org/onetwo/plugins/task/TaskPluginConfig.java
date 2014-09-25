package org.onetwo.plugins.task;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.task.entity.TaskConfig;
import org.springframework.util.Assert;

public class TaskPluginConfig implements LoadableConfig {

	private JFishProperties config;
	
	private TaskConfig taskConfig;

	public TaskPluginConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
		
		taskConfig = new TaskConfig();
		taskConfig.setTryTimes(config.getInt("try.times", 3));
		

		String dir = config.getProperty("attachment.dir");
		Assert.hasText(dir);
		taskConfig.setAttachmentDir(dir);
	}

	public TaskConfig getTaskConfig() {
		return taskConfig;
	}

	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}
	
	/*public String getQueueNamePrefix(){
		return config.getProperty("queue.name.prefix", "queue-");
	}*/
	

}
