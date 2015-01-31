package org.onetwo.plugins.task.webclient.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.task.TaskCoreConfig;
import org.onetwo.plugins.task.entity.TaskExecLog;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.entity.TaskQueueArchived;
import org.onetwo.plugins.task.utils.TaskConstant.QueueSourceTag;
import org.onetwo.plugins.task.utils.TaskConstant.TaskStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskQueueClientServiceImpl {
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Resource
	private TaskCoreConfig taskPluginConfig;
	
	public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	public void findExeLogPage(Page<TaskExecLog> page, Long taskQueueId){
		getBaseEntityManager().findPage(TaskExecLog.class, page, "taskQueueId", taskQueueId);
	}
	
	public TaskExecLog logExec(TaskExecLog log){
		getBaseEntityManager().save(log);
		return log;
	}
	
	public void findArchivedPage(Page<TaskQueueArchived> page, Object... properties){
		this.getBaseEntityManager().findPage(TaskQueueArchived.class, page, properties);
	}
	
	public void findPage(Page<TaskQueue> page){
		getBaseEntityManager().findPage(TaskQueue.class, page);
	}

	public void requeueArchived(Long archivedId){
		TaskQueueArchived archived = getBaseEntityManager().load(TaskQueueArchived.class, archivedId);
		TaskQueue queue = HibernateUtils.copyToTargetWithoutRelations(archived, TaskQueue.class, "id");
//		TaskQueue queue = new TaskQueue();
		queue.setSourceTag(QueueSourceTag.REQUEUE.toString());
		queue.setStatus(TaskStatus.WAITING);
		queue.setTask(archived.getTask());
		queue.setCurrentTimes(0);
		queue.setTaskCreateTime(new Date());
		queue.setLastExecTime(null);
		queue.setId(null);
		getBaseEntityManager().save(queue);
	}
}
