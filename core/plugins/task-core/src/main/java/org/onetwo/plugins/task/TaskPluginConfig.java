package org.onetwo.plugins.task;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class TaskPluginConfig implements LoadableConfig {

	private JFishProperties config;

	public TaskPluginConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
	}


	public int getTryTimes(){
		return config.getInt("try.times", 3);
	}
	
	/*public String getQueueNamePrefix(){
		return config.getProperty("queue.name.prefix", "queue-");
	}*/
	

}
