package org.onetwo.plugins.activemq;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class ActiveMQConfig extends AbstractLoadingConfig {

	private static final String ACTIVEMQ_BROKER_URL = "broker.url";
	
	private String brokerUrl;
	private List<String> queueNames;
	
	@Override
	protected void initConfig(JFishProperties config) {
		brokerUrl = config.getAndThrowIfEmpty(ACTIVEMQ_BROKER_URL);
		config.getPropertyWithSplit("queue", ",");
	}
	public String getBrokerUrl() {
		return brokerUrl;
	}
	public List<String> getQueueNames() {
		return queueNames;
	}

}
