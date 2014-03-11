package org.onetwo.plugins.activemq.model;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;


@Configuration
//@ComponentScan(basePackageClasses=PluginModelContext.class)
public class PluginModelContext {
	
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory(){
		ActiveMQConnectionFactory pcf = new ActiveMQConnectionFactory();
		pcf.setBrokerURL("tcp://92.168.1.132:61616");
		return pcf;
	}

	@Bean
	public SingleConnectionFactory singleConnectionFactory(){
		SingleConnectionFactory scf = new SingleConnectionFactory();
		scf.setTargetConnectionFactory(activeMQConnectionFactory());
		return scf;
	}

	@Bean
	public JmsTemplate jmsTemplate(){
		JmsTemplate jmst = new JmsTemplate();
		jmst.setConnectionFactory(singleConnectionFactory());
		return jmst;
	}
}
