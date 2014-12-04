package org.onetwo.plugins.akka.task;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.slf4j.Logger;

public class AkkaTaskConfig extends AbstractLoadingConfig { 

	protected static final Logger logger = MyLoggerFactory.getLogger(AkkaTaskConfig.class);

	

	@Override
	protected void initConfig(JFishProperties config) {
		
	}

	/***
	 * -1 is unlimited
	 * @return
	 */
	public boolean isLimitedQueue(){
		return getQueueMaxSize()>0;
	}
	
	public int getQueueMaxSize(){
		return getSourceConfig().getInt("queue.max.size", -1);
	}

}
