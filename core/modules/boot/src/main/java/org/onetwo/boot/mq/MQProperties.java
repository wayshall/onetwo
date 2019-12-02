package org.onetwo.boot.mq;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.NetUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

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
	
	public static final String TRANSACTIONAL_SEND_TASK_CONFIG_KEY = PREFIX_KEY+".sendTask.fixedDelayString";

	public static final String TRANSACTIONAL_SEND_TASK_ENABLED_KEY = PREFIX_KEY+".transactional.sendTask.enabled";

	public static final String TRANSACTIONAL_DELETE_TASK_ENABLED_KEY = PREFIX_KEY+".transactional.deleteTask.enabled";
	
	/***
	 * 默认半夜两点触发
	 */
	public static final String TRANSACTIONAL_DELETE_TASK_CRON = "${jfish.mq.transactional.deleteTask.cron: 0 0 2 * * ?}";
//	public static final String TRANSACTIONAL_DELETE_TASK_CRON = "${jfish.mq.transactional.deleteTask.cron: 0 11 16 * * ?}"; //for test

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
		int sendCountPerTask = 1000;
		//忽略最近时间创建的消息，默认1分钟
		@Deprecated
		String ignoreCreateAtRecently;// = "30s";
		//新版本补偿发送，向前扫描的时间间隔
		String scanForwardTime = "30m";
		//锁定的key
		String locker;
		private String redisLockTimeout;
		private boolean checkMessageTable = true;
		
		public String getLocker(){
			String locker = this.locker;
			if(StringUtils.isNotBlank(locker)){
				return locker;
			}

			this.locker = NetUtils.getHostAddress();
			Assert.hasText(this.locker, "send task locker can not be null");
			return this.locker;
		}
	}
	@Data
	public static class DeleteTaskProps {
		TaskLocks lock = TaskLocks.DB;
		String deleteBeforeAt;
		private String redisLockTimeout;
	}

	public static enum TaskLocks {
		DB,
		REDIS
	}
}
