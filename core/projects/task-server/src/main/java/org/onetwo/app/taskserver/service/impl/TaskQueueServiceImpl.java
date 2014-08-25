package org.onetwo.app.taskserver.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskConstant.TaskStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TaskQueueServiceImpl {
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	public List<TaskQueue> loadAllExecuting(){
		List<TaskQueue> queues = this.baseEntityManager.findByProperties(TaskQueue.class, "status", TaskStatus.EXECUTING);
		return queues;
	}
	
	public List<TaskQueue> loadAndLockWaiting(int size){
		List<TaskQueue> queues = this.baseEntityManager.findByProperties(TaskQueue.class, "status", TaskStatus.WAITING, K.MAX_RESULTS, size);
		for(TaskQueue tq : queues){
			tq.setStatus(TaskStatus.EXECUTING);
		}
		return queues;
	}

}
