package org.onetwo.ext.rocketmq.config;

import org.onetwo.boot.mq.MQProperties;
import org.onetwo.boot.mq.MQTaskConfiguration;
import org.onetwo.boot.mq.task.CompensationSendMessageTask;
import org.onetwo.boot.mq.task.DeleteSentMessageTask;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.task.DeleteReceiveMessageTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 不使用 MQProperties#TRANSACTIONAL_SEND_TASK_ENABLED_KEY 配置控制是否激活mq的定时任务，
 * 而直接使用代码配置的方式激活mq的定时任务
 * 
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableScheduling
public class TransactionalMQTaskConfiguration extends MQTaskConfiguration {
	@Autowired
	private ONSProperties onsProperties;

	public TransactionalMQTaskConfiguration(MQProperties mqProperties) {
		super(mqProperties);
	}
	
	@Bean
	public CompensationSendMessageTask compensationSendMessageTask(){
		return super.createCompensationSendMessageTask();
	}

	@Bean
	public DeleteSentMessageTask deleteSentMessageTask(){
		return super.createDeleteSentMessageTask();
	}
	
	@Bean
	public DeleteReceiveMessageTask deleteReceiveMessageTask() {
		DeleteReceiveMessageTask task = new DeleteReceiveMessageTask();
		task.setRedisLockTimeout(onsProperties.getDeleteReceiveTask().getRedisLockTimeout());
		task.setDeleteBeforeAt(onsProperties.getDeleteReceiveTask().getDeleteBeforeAt());
		return task;
	}
}

