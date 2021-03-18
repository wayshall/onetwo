package org.onetwo.ext.ons;

import java.util.Map;
import java.util.Properties;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.date.NiceDate;
import org.onetwo.ext.alimq.JsonMessageDeserializer;
import org.onetwo.ext.alimq.JsonMessageSerializer;
import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.alimq.MessageSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.google.common.collect.Maps;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties("jfish.ons")
public class ONSProperties implements InitializingBean {
	public static final String PRODUCER_ENABLED_KEY = "jfish.ons.producer.enabled";
//	public static final String TRANSACTIONAL_ENABLED_KEY = "jfish.ons.transactional.enabled";
//	public static final String TRANSACTIONAL_TASK_CRON_KEY = "jfish.ons.transactional.task.cron";
//	public static final String TRANSACTIONAL_DELETE_TASK_CRON_KEY = "jfish.ons.transactional.deleteTask.cron";


	MqServerTypes serverType = MqServerTypes.ONS;
	
	String accessKey;
	String secretKey;
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
	public Properties baseProperties(){
		Properties baseConfig = new Properties();
		baseConfig.putAll(commons);
		if(MqServerTypes.ROCKETMQ==serverType){
			//自动填写默认值，避免ons客户端抛错
			this.accessKey = "<rocketmq don't need accessKey>";
			this.secretKey = "<rocketmq don't need secretKey>";
			Assert.hasText(namesrvAddr, "namesrvAddr must have text");
			baseConfig.setProperty(PropertyKeyConst.NAMESRV_ADDR, namesrvAddr);
		}
		Assert.hasText(onsAddr, "onsAddr must have text");
		baseConfig.setProperty(PropertyKeyConst.AccessKey, accessKey);
		baseConfig.setProperty(PropertyKeyConst.SecretKey, secretKey);
		baseConfig.setProperty(PropertyKeyConst.ONSAddr, onsAddr);
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
		ONS,
		ROCKETMQ
	}
}
