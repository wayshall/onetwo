package org.onetwo.plugins.activemq.model;

import java.util.Properties;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactoryBean;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;


@Configuration
//@ComponentScan(basePackageClasses=PluginModelContext.class)
public class PluginModelContext {

	private static final String ACTIVEMQ_CONFIG_BASE = "/activemq/activemq-config";
	public static final String ACTIVEMQ_CONFIG_PATH = ACTIVEMQ_CONFIG_BASE + ".properties";
	
	@Resource
    private ConnectionFactory connectionFactory;

	@Resource
	private AppConfig appConfig;
	
	@Resource
	private Properties activemqConfig;

	@Bean
	public PropertiesFactoryBean activemqConfig() {
		String envLocation = ACTIVEMQ_CONFIG_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(ACTIVEMQ_CONFIG_PATH, envLocation);
	}
	
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory(){
		ActiveMQConnectionFactory pcf = new ActiveMQConnectionFactory();
		pcf.setBrokerURL("tcp://92.168.1.132:61616");
		return pcf;
	}

	/*@Bean
	public SingleConnectionFactory singleConnectionFactory(){
		SingleConnectionFactory scf = new SingleConnectionFactory();
		scf.setTargetConnectionFactory(activeMQConnectionFactory());
		return scf;
	}*/

	@Bean
	public PooledConnectionFactoryBean connectionFactory(){
		PooledConnectionFactoryBean pcfb = new PooledConnectionFactoryBean();
		pcfb.setConnectionFactory(activeMQConnectionFactory());
		pcfb.setMaxConnections(10);
		return pcfb;
	}

	@Bean
	public JmsTemplate jmsTemplate(){
		JmsTemplate jmst = new JmsTemplate();
		jmst.setConnectionFactory(this.connectionFactory);
		return jmst;
	}
}
