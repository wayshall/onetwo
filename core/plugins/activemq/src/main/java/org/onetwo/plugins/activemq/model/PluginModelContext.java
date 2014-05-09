package org.onetwo.plugins.activemq.model;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.activemq.MessageReceiver;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;


@Configuration
//@ComponentScan(basePackageClasses=PluginModelContext.class)
public class PluginModelContext implements InitializingBean {

	private static final String ACTIVEMQ_BROKER_URL = "broker.url";
	private static final String ACTIVEMQ_CONFIG_BASE = "/activemq/activemq-config";
	public static final String ACTIVEMQ_CONFIG_PATH = ACTIVEMQ_CONFIG_BASE + ".properties";
	
	private static final Logger logger = MyLoggerFactory.getLogger(PluginModelContext.class);
	
	@Resource
	private ApplicationContext applicationContext;
	
//	@Resource
//    private ConnectionFactory connectionFactory;

	@Resource
	private AppConfig appConfig;
	
	@Resource
	private Properties activemqConfig;
//	private PropertiesWraper activemqConfigWraper;

	@Bean
	public PropertiesFactoryBean activemqConfig() {
		String envLocation = ACTIVEMQ_CONFIG_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(ACTIVEMQ_CONFIG_PATH, envLocation);
	}

	@Bean
	public JFishProperties activemqConfigWrapper() {
		JFishProperties activemqConfigWraper = JFishProperties.wrap(activemqConfig);
		return activemqConfigWraper;
	}
	
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory(){
		ActiveMQConnectionFactory pcf = new ActiveMQConnectionFactory();
		JFishProperties activemqConfigWraper = activemqConfigWrapper();
		String brokerURL = activemqConfigWraper.getAndThrowIfEmpty(ACTIVEMQ_BROKER_URL);
//		String brokerURL = "tcp://localhost:61616";
		logger.info("activemq server broker url: " + brokerURL);
		pcf.setBrokerURL(brokerURL);
		return pcf;
	}

	@Bean
	public SingleConnectionFactory connectionFactory(){
		SingleConnectionFactory scf = new SingleConnectionFactory();
		scf.setTargetConnectionFactory(activeMQConnectionFactory());
		return scf;
	}

	/*@Bean
	public PooledConnectionFactoryBean connectionFactory(){
		PooledConnectionFactoryBean pcfb = new PooledConnectionFactoryBean();
		pcfb.setConnectionFactory(activeMQConnectionFactory());
		pcfb.setMaxConnections(10);
		return pcfb;
	}*/

	@Bean
	public JmsTemplate jmsTemplate(){
		JmsTemplate jmst = new JmsTemplate();
		jmst.setConnectionFactory(connectionFactory());
		return jmst;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		JFishProperties activemqConfigWraper = activemqConfigWrapper();
		
		List<String> queueNames = activemqConfigWraper.getPropertyWithSplit("queue", ",");
		for(String queueName :queueNames){
			SpringUtils.registerBean(applicationContext, queueName, ActiveMQQueue.class, "physicalName", queueName);
			logger.info("registered activemq queue : " + queueName);
		}
		
		ConnectionFactory connectionFactory = connectionFactory();
		List<MessageReceiver> messageReceivers = SpringUtils.getBeans(applicationContext, MessageReceiver.class);
		for(MessageReceiver<Object> messageReceiver : messageReceivers){
			MessageListenerAdapter adapter = new MessageListenerAdapter(messageReceiver);
			if(messageReceiver.getMessageConverter()!=null)
				adapter.setMessageConverter(messageReceiver.getMessageConverter());
			
//			DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
			Assert.notNull(connectionFactory);
			Destination des = (Destination) applicationContext.getBean(messageReceiver.getDestinationName());
			if(des==null)
				throw new BaseException("can not found Destination: " + messageReceiver.getDestinationName());
//			container.setDestination(des);
			String beanName = DefaultMessageListenerContainer.class.getSimpleName()+"#"+messageReceiver.getDestinationName();
			SpringUtils.registerBean(applicationContext, beanName, DefaultMessageListenerContainer.class, 
					"connectionFactory", connectionFactory, "messageListener", adapter, "destination", des);
			logger.info("registered activemq messageReceiver for " + messageReceiver.getDestinationName());
		}
	}
	
	
}
