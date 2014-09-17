package org.onetwo.plugins.task;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.springframework.util.Assert;

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
	
	public String getAttachmentDir(){
		String dir = config.getProperty("attachment.dir");
		Assert.hasText(dir);
		return dir;
	}

	public String getAttachmentPath(String path){
		String realpath = getAttachmentDir() + StringUtils.appendStartWith(path, "/");
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
