package org.onetwo.boot.module.activemq;

import lombok.Data;

import org.apache.activemq.RedeliveryPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(value=ActivemqProperties.PREFIX_KEY)
@Data
public class ActivemqProperties {
	public static final String PREFIX_KEY = "jfish.activemq";
	public static final String ENABLE_KEY = PREFIX_KEY+".enabled";
	public static final String EMBEDDED_ENABLE_KEY = PREFIX_KEY+".embedded.enabled";
	
//	Properties connectionFactory = new Properties();
	KahaDBStoreProps kahadbStore = new KahaDBStoreProps();
	JdbcStoreProps jdbcStore = new JdbcStoreProps();
	RedeliveryProps redelivery = new RedeliveryProps();
	
	@Data
	static public class KahaDBStoreProps {
		public static final String ENABLE_KEY = "jfish.activemq.kahadbStore.enabled";
		String dataDir;
	}
	
	@Data
	static public class JdbcStoreProps {
		public static final String ENABLE_KEY = "jfish.activemq.jdbcStore.enabled";
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
		     * activemq默认为false，这里修改为true
		     */
		    boolean useExponentialBackOff = true;
		    double backOffMultiplier = 5.0;
		    long redeliveryDelay = initialRedeliveryDelay;
		}
	}
	

}
