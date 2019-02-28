package org.onetwo.ext.ons;

import java.util.Map;
import java.util.Properties;

import org.onetwo.ext.alimq.JsonMessageDeserializer;
import org.onetwo.ext.alimq.JsonMessageSerializer;
import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.alimq.MessageSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.google.common.collect.Maps;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties(org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".ons")
public class ONSProperties implements InitializingBean {

//	public static final String TRANSACTIONAL_ENABLED_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".ons.transactional.enabled";
//	public static final String TRANSACTIONAL_TASK_CRON_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".ons.transactional.task.cron";
//	public static final String TRANSACTIONAL_DELETE_TASK_CRON_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".ons.transactional.deleteTask.cron";


	MqServerTypes serverType = MqServerTypes.ONS;
	
	String accessKey;
	String secretKey;
	String onsAddr;
	String namesrvAddr;
	MessageSerializerType serializer =  MessageSerializerType.JSON;
	Properties commons = new Properties();
	Map<String, Properties> producers = Maps.newHashMap();
	Map<String, Properties> consumers = Maps.newHashMap();
	
	DeleteReceiveTask deleteReceiveTask = new DeleteReceiveTask();
	
//	Map<String, String> jsonDeserializerCompatibilityTypeMappings = Maps.newHashMap();	

	public Map<String, Properties> getProducers() {
		return producers;
	}

	public Map<String, Properties> getConsumers() {
		return consumers;
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
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
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
		CHECKED_JSON(JsonMessageSerializer.CHECKED_INSTANCE, JsonMessageDeserializer.INSTANCE);

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
