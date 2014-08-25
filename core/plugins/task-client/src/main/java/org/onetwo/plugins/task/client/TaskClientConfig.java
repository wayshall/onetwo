package org.onetwo.plugins.task.client;

import java.util.Properties;

import org.onetwo.common.utils.propconf.JFishProperties;

public class TaskClientConfig {

	private final JFishProperties config;

	public TaskClientConfig(Properties clientConfig) {
		super();
		this.config = JFishProperties.wrap(clientConfig);
	}

	public int getTryTimes(){
		return config.getInt("try.times", 3);
	}
	
	public String getQueueNamePrefix(){
		return config.getProperty("queue.name.prefix", "queue-");
	}
	

}
