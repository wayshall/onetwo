package org.onetwo.boot.mq;

import org.onetwo.boot.mq.MQProperties.DeleteTaskProps;
import org.onetwo.boot.mq.MQProperties.TaskLocks;
import org.onetwo.boot.mq.task.DelayableSendMessageTask;
import org.onetwo.boot.mq.task.DeleteSentMessageTask;
import org.onetwo.boot.mq.task.SendMessageTask;

/**
 * @author wayshall
 * <br/>
 */
public class MQTaskConfiguration {

	private MQProperties mqProperties;
	
	public MQTaskConfiguration(MQProperties mqProperties) {
		super();
		this.mqProperties = mqProperties;
	}

	public SendMessageTask createSendMessageTask(){
		DelayableSendMessageTask task = new DelayableSendMessageTask();
		TaskLocks taskLock = mqProperties.getTransactional().getSendTask().getLock();
		if(taskLock==TaskLocks.REDIS){
			task.setUseReidsLock(true);
		}
		task.setRedisLockTimeout(mqProperties.getTransactional().getSendTask().getRedisLockTimeout());
		return task;
	}
	
	public DeleteSentMessageTask createDeleteSentMessageTask(){
		DeleteSentMessageTask task = new DeleteSentMessageTask();
		DeleteTaskProps deleteProps = mqProperties.getTransactional().getDeleteTask();
		if(deleteProps.getLock()==TaskLocks.REDIS){
			task.setUseReidsLock(true);
		} else if (deleteProps.getLock()==TaskLocks.DB) {
			task.setUseReidsLock(false);
		}
		task.setDeleteBeforeAt(deleteProps.getDeleteBeforeAt());
		task.setRedisLockTimeout(deleteProps.getRedisLockTimeout());
		return task;
	}
}
