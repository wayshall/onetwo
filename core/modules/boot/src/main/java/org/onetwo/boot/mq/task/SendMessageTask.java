package org.onetwo.boot.mq.task;

/**
 * @author weishao zeng
 * <br/>
 */
public interface SendMessageTask {

//	@Scheduled(fixedDelayString="${"+MQProperties.TRANSACTIONAL_SEND_TASK_CONFIG_KEY+"}", initialDelay=10000) don't work
	void scheduleCheckSendMessage();
}

