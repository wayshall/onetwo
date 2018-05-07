package org.onetwo.boot.mq;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(value=MQProperties.PREFIX_KEY)
@Data
public class MQProperties {
	public static final String PREFIX_KEY = "jfish.mq";
	public static final String ENABLE_KEY = PREFIX_KEY+".enabled";

	public static final String TRANSACTIONAL_ENABLED_KEY = PREFIX_KEY+".transactional.enabled";
	
	public static final String TRANSACTIONAL_SEND_TASK_FIXED_RATE_STRING_KEY = PREFIX_KEY+".sendTask.fixedRateString";

	public static final String TRANSACTIONAL_SEND_TASK_ENABLED_KEY = PREFIX_KEY+".transactional.sendTask.enabled";

	public static final String TRANSACTIONAL_DELETE_TASK_ENABLED_KEY = PREFIX_KEY+".transactional.deleteTask.enabled";
	
	/***
	 * 默认半夜两点触发
	 */
	public static final String TRANSACTIONAL_DELETE_TASK_CRON = "${jfish.mq.transactional.deleteTask.cron:0 0 2 * * ?}";
//	public static final String TRANSACTIONAL_DELETE_TASK_CRON = "${jfish.mq.transactional.deleteTask.cron:0 11 16 * * ?}"; //for test

	TransactionalProps transactional = new TransactionalProps();
	
	@Data
	static public class TransactionalProps {
		SendMode sendMode = SendMode.SYNC;
		SendTaskProps sendTask = new SendTaskProps();
		DeleteTaskProps deleteTask = new DeleteTaskProps();
	}
	public static enum SendMode {
		SYNC,
		ASYNC
	}
	
	@Data
	public static class SendTaskProps {
		TaskLocks lock = TaskLocks.DB;
		int sendCountPerTask = 300;
		//忽略最近时间创建的消息，默认1分钟
		String ignoreCreateAtRecently = "1m";
	}
	@Data
	public static class DeleteTaskProps {
		TaskLocks lock = TaskLocks.DB;
		String deleteBeforeAt;
	}

	public static enum TaskLocks {
		DB,
		REDIS
	}
}
