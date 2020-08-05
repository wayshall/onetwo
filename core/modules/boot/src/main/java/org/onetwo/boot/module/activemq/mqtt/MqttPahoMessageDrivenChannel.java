package org.onetwo.boot.module.activemq.mqtt;

import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.InBoundClientProps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

/**
 * @author weishao zeng
 * <br/>
 */

public class MqttPahoMessageDrivenChannel extends MqttPahoMessageDrivenChannelAdapter implements InitializingBean {

	private InBoundClientProps clientConfig;
	
	public MqttPahoMessageDrivenChannel(InBoundClientProps clientConfig, MqttPahoClientFactory clientFactory) {
		super(clientConfig.getClientId(), clientFactory, clientConfig.getTopics());
		this.clientConfig = clientConfig;
	}

	@Override
	public void onInit() {
		setQos(clientConfig.getQos());
		setCompletionTimeout(clientConfig.getCompletionTimeout());
		
		super.onInit();
	}

}
