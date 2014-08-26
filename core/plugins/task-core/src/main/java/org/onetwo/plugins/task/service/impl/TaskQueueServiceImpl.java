package org.onetwo.plugins.task.service.impl;

import java.util.List;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskConstant.TaskStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskQueueServiceImpl extends HibernateCrudServiceImpl<TaskQueue, Long>{
	
	public List<TaskQueue> loadAllExecuting(){
		List<TaskQueue> queues = findByProperties(TaskQueue.class, "status", TaskStatus.EXECUTING);
		return queues;
	}
	
	public List<TaskQueue> loadAndLockWaiting(int size){
		List<TaskQueue> queues = findByProperties(TaskQueue.class, "status", TaskStatus.WAITING, K.MAX_RESULTS, size);
		for(TaskQueue tq : queues){
			tq.setStatus(TaskStatus.EXECUTING);
		}
		return queues;
	}

}
