package org.onetwo.boot.module.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.onetwo.common.spring.copier.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQConnectionFactoryCustomizer;

/**
 * @author wayshall
 * <br/>
 */
public class ConfigActiveMQConnectionFactory implements ActiveMQConnectionFactoryCustomizer {
	
	@Autowired
	private ActivemqProperties activemqProperties;

	@Override
	public void customize(ActiveMQConnectionFactory factory) {
		RedeliveryPolicyMap rpm = factory.getRedeliveryPolicyMap();
		if(rpm!=null && rpm.getDefaultEntry()!=null){
			RedeliveryPolicy def = rpm.getDefaultEntry();
			CopyUtils.copy(def, activemqProperties.getRedelivery().getDefaultPolicy());
		}
		factory.setPrefetchPolicy(activemqProperties.getPrefetchPolicy());
	}

}
