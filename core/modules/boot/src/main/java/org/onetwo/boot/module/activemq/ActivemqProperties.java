package org.onetwo.boot.module.activemq;

import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(value=ActivemqProperties.PREFIX_KEY)
@Data
public class ActivemqProperties {
	public static final String PREFIX_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq";
	public static final String TYPE_KEY = PREFIX_KEY+".type";
	public static final String ENABLE_KEY = PREFIX_KEY+".enabled";
	public static final String EMBEDDED_ENABLE_KEY = PREFIX_KEY+".embedded.enabled";
	/***
	 * jfish.activemq.message.converter
	 */
	public static final String CONVERTER_KEY = PREFIX_KEY+".message.converter";
	

//	public static final String MESSAGE_CONVERTER_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.messageConverter";
	
//	Properties connectionFactory = new Properties();
	KahaDBStoreProps kahadbStore = new KahaDBStoreProps();
	JdbcStoreProps jdbcStore = new JdbcStoreProps();
	RedeliveryProps redelivery = new RedeliveryProps();
//	String messageConverter = "simple";
	
	TopicProps topic = new TopicProps();
	
	ActiveMQTypes type;
	
	/***
	 * 消息预取策略
	 * jfish.activemq:
	 * 		prefetchPolicy:
	 * 			queuePrefetch: 1
	 * 			topicPrefetch: 1
	 */
	ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
	
	@Data
	static public class TopicProps {
		Boolean subscriptionDurable;
		Boolean subscriptionShared;
		String clientId;
	}
	@Data
	static public class KahaDBStoreProps {
		public static final String ENABLE_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.kahadbStore.enabled";
		String dataDir;
	}
	
	@Data
	static public class JdbcStoreProps {
		public static final String ENABLE_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.jdbcStore.enabled";
		boolean createTablesOnStartup = true;
	}
	
	@Data
	class RedeliveryProps {
		RedeliveryPolicyProps defaultPolicy = new RedeliveryPolicyProps();
		/***
		 * 初始化默认使用activemq默认值
		 * @author wayshall
		 *
		 */
		@Data
		class RedeliveryPolicyProps {
			int maximumRedeliveries = RedeliveryPolicy.DEFAULT_MAXIMUM_REDELIVERIES;
		    double collisionAvoidanceFactor = 0.15d;
		    /***
		     * 最大延迟时间，当useExponentialBackOff为true的时候，可以防止时间过大，一直阻塞队列继续往前消费
		     */
		    long maximumRedeliveryDelay = -1;
		    long initialRedeliveryDelay = 1000L;
		    boolean useCollisionAvoidance;
		    /***
		     * 重发时是否成倍地增大：nextDelay = (long) (previousDelay * backOffMultiplier);
		     * activemq默认为false
		     */
		    boolean useExponentialBackOff = false;
		    double backOffMultiplier = 5.0;
		    long redeliveryDelay = initialRedeliveryDelay;
		}
	}
	
}
