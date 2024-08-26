package org.onetwo.ext.ons;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.date.NiceDate;
import org.onetwo.ext.alimq.JsonMessageDeserializer;
import org.onetwo.ext.alimq.JsonMessageSerializer;
import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.alimq.MessageSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties(ONSProperties.ONS_PREFIX)
public class ONSProperties implements InitializingBean {
	public static final String ONS_PREFIX = BootJFishConfig.ZIFISH_CONFIG_PREFIX + ".ons";
	
	public static final String ONS_ENABLE_KEY = ONS_PREFIX + ".onsAddr";
	public static final String PRODUCER_ENABLED_KEY = ONS_PREFIX + ".producer.enabled";
//	public static final String TRANSACTIONAL_ENABLED_KEY = "jfish.ons.transactional.enabled";
//	public static final String TRANSACTIONAL_TASK_CRON_KEY = "jfish.ons.transactional.task.cron";
//	public static final String TRANSACTIONAL_DELETE_TASK_CRON_KEY = "jfish.ons.transactional.deleteTask.cron";

	MqServerTypes serverType = MqServerTypes.ROCKETMQ;
	
//	String accessKey;
//	String secretKey;
	String onsAddr;
	String namesrvAddr;
	MessageSerializerType serializer =  MessageSerializerType.JSON;
	Properties commons = new Properties();
	Map<String, Properties> producers = Maps.newHashMap();
	/***
	 * 配置方式：
	 * jfish.ons.consumers.consumerId.consumeTimeout: 15
	 */
	Map<String, Properties> consumers = Maps.newHashMap();
	String consumerPrefix;
	
	/***
	 * 用于特殊情况下，统一把所有consumer从某个时间段开始订阅
	 */
	ConsumeFromWhereProps specialConsume = new ConsumeFromWhereProps();
	
	DeleteReceiveTask deleteReceiveTask = new DeleteReceiveTask();
	
	/****
	 * 消费者初始化出错的时候是否抛出异常
	 * 有时候程序简单的测试并不需要连接mq，即使初始化错误也没问题
	 */
	boolean throwOnCusomerInitializeError = true;
	
//	Map<String, String> jsonDeserializerCompatibilityTypeMappings = Maps.newHashMap();	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	public Map<String, Properties> getProducers() {
		return producers;
	}

	public Map<String, Properties> getConsumers() {
		return consumers;
	}

	public MessageSerializerType getSerializer() {
		return serializer;
	}
	
	public String getNamesrvAddr() {
		if (StringUtils.isNotBlank(namesrvAddr)) {
			return namesrvAddr;
		}
		return onsAddr;
	}
	public Properties baseProperties(){
		Properties baseConfig = new Properties();
		baseConfig.putAll(commons);
		if(MqServerTypes.ROCKETMQ==serverType){
			//自动填写默认值，避免ons客户端抛错
//			this.accessKey = "<rocketmq don't need accessKey>";
//			this.secretKey = "<rocketmq don't need secretKey>";
			String namesrvAddr = getNamesrvAddr();
			Assert.hasText(namesrvAddr, "namesrvAddr must have text");
//			baseConfig.setProperty(PropertyKeyConst.NAMESRV_ADDR, namesrvAddr);
		}
//		Assert.hasText(onsAddr, "onsAddr must have text");
//		baseConfig.setProperty(PropertyKeyConst.AccessKey, accessKey);
//		baseConfig.setProperty(PropertyKeyConst.SecretKey, secretKey);
//		baseConfig.setProperty(PropertyKeyConst.ONSAddr, onsAddr);
		return baseConfig;
	}
	
	/***
	 * 用于特殊情况下，统一把所有consumer从某个时间段开始订阅……
	 * @author way
	 *
	 */
	@Data
	public static class ConsumeFromWhereProps {
		boolean enabled;
		ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_TIMESTAMP;
		/***
		 * 默认为启动时间
		 */
		String consumeTimestamp = NiceDate.Now().format(DateUtils.DATETIME);
	}
	
	@Data
	public static class DeleteReceiveTask {
		/***
		 * 默认半夜3点触发
		 */
		public static final String DELETE_RECEIVE_TASK_CRON = "${jfish.ons.deleteReceiveTask.cron: 0 0 3 * * ?}";
		private String deleteBeforeAt;
		private String redisLockTimeout;
		
	}

	public static enum MessageSerializerType {
		JDK(MessageSerializer.DEFAULT, MessageDeserializer.DEFAULT),
		JSON(JsonMessageSerializer.INSTANCE, JsonMessageDeserializer.INSTANCE),
		/***
		 * 检查消息是否可以用反射实例化，保证反序列化时不会因为没有默认构造函数而出错
		 */
		CHECKED_JSON(JsonMessageSerializer.CHECKED_INSTANCE, JsonMessageDeserializer.INSTANCE),
		TYPING_JSON(JsonMessageSerializer.TYPING_INSTANCE, JsonMessageDeserializer.TYPING_INSTANCE);

		final private MessageSerializer serializer;
		final private MessageDeserializer deserializer;
		private MessageSerializerType(MessageSerializer serializer,
				MessageDeserializer deserializer) {
			this.serializer = serializer;
			this.deserializer = deserializer;
		}
		public MessageSerializer getSerializer() {
			return serializer;
		}
		public MessageDeserializer getDeserializer() {
			return deserializer;
		}
		
	}

	public static enum MqServerTypes {
		/****
		 * 支持任意时刻的延迟消息
		 * 支持事务消息
		 * 
		 * 不再支持ons
		 */
		@Deprecated
		ONS,
		ROCKETMQ
	}
}
