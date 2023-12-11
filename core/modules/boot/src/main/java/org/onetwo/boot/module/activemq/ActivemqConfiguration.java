package org.onetwo.boot.module.activemq;

import javax.jms.ConnectionFactory;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.module.activemq.ActivemqProperties.TopicProps;
import org.onetwo.boot.module.activemq.artemis.ActiveMQArtemisConfiguration;
import org.onetwo.boot.module.activemq.classic.ActiveMQClassicConfiguration;
import org.onetwo.boot.module.jms.JmsConfiguration;
import org.onetwo.boot.module.jms.JmsProducerService;
import org.onetwo.boot.module.jms.JmsUtils.ContainerFactorys;
import org.onetwo.boot.mq.MQTransactionalConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * JmsAutoConfiguration
 * @author wayshall
 * <br/>
 */
@ConditionalOnClass(EnableJms.class)
@ConditionalOnProperty(value=ActivemqProperties.ENABLE_KEY)
@EnableJms
@EnableConfigurationProperties(ActivemqProperties.class)
@Configuration
@Import({
	MQTransactionalConfiguration.class,
	ActiveMQClassicConfiguration.class,
	ActiveMQArtemisConfiguration.class,
	
	JmsConfiguration.class
})
public class ActivemqConfiguration implements InitializingBean {
	
	/*@Autowired
	private ActiveMQConnectionFactory activeMQConnectionFactory;*/
	@Autowired
	private ActivemqProperties activemqProperties;
	@Autowired(required = false)
	private MessageConverter messageConverter;
	
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		/*Properties cfProps = activemqProperties.getConnectionFactory();
		
		String trustedPackages = (String)cfProps.remove("trustedPackages");
		List<String> trustedPackagesList = Lists.newArrayList(activeMQConnectionFactory.getTrustedPackages());
		trustedPackagesList.add(JmsMessage.class.getPackage().getName());
		trustedPackagesList.addAll(Arrays.asList(GuavaUtils.split(trustedPackages, ",")));
		
		BeanPropertiesMapper bpm = new BeanPropertiesMapper(cfProps, "", true);
		bpm.mapToObject(activeMQConnectionFactory);
		activeMQConnectionFactory.setTrustedPackages(trustedPackagesList);*/
	}
	
	@Bean
	public ConfigActiveMQConnectionFactory configActiveMQConnectionFactory(){
		return new ConfigActiveMQConnectionFactory();
	}

	@Bean(name=ContainerFactorys.QUEUE)
	public DefaultJmsListenerContainerFactory queueListenerContainerFactory(
			DefaultJmsListenerContainerFactoryConfigurer configurer,
			ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(false);
		if (messageConverter!=null) {
			factory.setMessageConverter(messageConverter);
		}
		return factory;
	}
	
	@Bean(name=ContainerFactorys.TOPIC)
	public DefaultJmsListenerContainerFactory topicListenerContainerFactory(
			DefaultJmsListenerContainerFactoryConfigurer configurer,
			ConnectionFactory connectionFactory) {
		TopicProps topic = activemqProperties.getTopic();
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(true);
		factory.setSubscriptionDurable(topic.getSubscriptionDurable());
		factory.setSubscriptionShared(topic.getSubscriptionShared());
		factory.setClientId(topic.getClientId());
		if (messageConverter!=null) {
			factory.setMessageConverter(messageConverter);
		}
		return factory;
	}

	@Bean
	public JmsProducerService jmdProducerService(){
		JmsProducerService producer = new JmsProducerService();
		return producer;
	}
	
	@Configuration
	@ConditionalOnProperty(name = ActivemqProperties.CONVERTER_KEY, havingValue = "jackson2", matchIfMissing = false)
	static public class JacksonMessageConverterConfiguration {
		
		@Bean
		public MessageConverter jackson2MessageConverter() {
			MappingJackson2MessageConverter jackson = new MappingJackson2MessageConverter();
			jackson.setTypeIdPropertyName("__" + BootJFishConfig.ZIFISH_CONFIG_PREFIX + "_jackson2_type__");
			return jackson;
		}
	}
	
	

//	@Configuration
//	@ConditionalOnClass(BrokerService.class)
//	@ConditionalOnProperty(value=ActivemqProperties.EMBEDDED_ENABLE_KEY)
//	protected static class BrokerServiceConfiguration {
//		@ConditionalOnMissingBean
//		@Bean(initMethod="start", destroyMethod="stop")
//		public BrokerService brokerService(PersistenceAdapter persistenceAdapter) throws IOException{
//			BrokerService broker = new BrokerService();
//			broker.setPersistenceAdapter(persistenceAdapter);
//			return broker;
//		}
//	}
	
//	@Configuration
//	@ConditionalOnProperty(KahaDBStoreProps.ENABLE_KEY)
//	protected static class KahaDBPersistenceStoreConfiguration {
//		@Autowired
//		ActivemqProperties activemqProperties;
//		
//		@Bean
//		public PersistenceAdapter kahaDBPersistenceAdapter(){
//			KahaDBStoreProps kahaProps = activemqProperties.getKahadbStore();
//			// default messages store is under AMQ_HOME/data/KahaDB/
//			KahaDBPersistenceAdapter kahadb = new KahaDBPersistenceAdapter();
//			if(StringUtils.isNotBlank(kahaProps.getDataDir())){
//				File dataDir = new File(kahaProps.getDataDir());
//				if(!dataDir.exists()){
//					dataDir.mkdirs();
//				}
//				kahadb.setDirectory(dataDir);
//			}
//			return kahadb;
//		}
//	}
//	
//	@Configuration
//	@ConditionalOnProperty(JdbcStoreProps.ENABLE_KEY)
//	protected static class JdbcDBPersistenceStoreConfiguration {
//		@Autowired
//		ActivemqProperties activemqProperties;
//		@Bean
//		public PersistenceAdapter jdbcPersistenceAdapter(DataSource dataSource){
//			JdbcStoreProps jdbcProps = activemqProperties.getJdbcStore();
//			JDBCPersistenceAdapter jdbc = new JDBCPersistenceAdapter(dataSource, new OpenWireFormat());
//			jdbc.setCreateTablesOnStartup(jdbcProps.isCreateTablesOnStartup());
//			return jdbc;
//		}
//	}

	/*
	@Configuration
	protected static class JmsMessageConvertorConfiguration {
		@Autowired
		ActivemqProperties activemqProperties;
		
		@Bean
//		@ConditionalOnMissingBean
//		@ConditionalOnSingleCandidate(JmsTemplate.class)
		@ConditionalOnProperty(value=ActivemqProperties.MESSAGE_CONVERTER_KEY)
		public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
			MessageConverter messageConverter = null;
			switch (activemqProperties.getMessageConverter()) {
				case "simple":
					messageConverter = new SimpleMessageConverter();
					break;
				case "jackson2":
					messageConverter = new MappingJackson2MessageConverter();
					break;
				case "string":
					messageConverter = new StringMessageConverter();
					break;
				case "byteArray":
					messageConverter = new ByteArrayMessageConverter();
					break;
				case "marshalling":
					messageConverter = new MarshallingMessageConverter();
					break;
				default:
					messageConverter = ReflectUtils.newInstance(activemqProperties.getMessageConverter());
					break;
			}
			JmsMessagingTemplate jms = new JmsMessagingTemplate(jmsTemplate);
			jms.setMessageConverter(messageConverter);
			return jms;
		}
	}
	*/
}
