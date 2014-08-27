package org.onetwo.plugins.task.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.plugins.task.TaskPluginConfig;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskConstant.TaskStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskQueueServiceImpl extends HibernateCrudServiceImpl<TaskQueue, Long>{
	
	@Resource
	private TaskPluginConfig taskPluginConfig;
	
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

	/***
	 * 添加到数据库队列
	 * @param queue
	 */
	public TaskQueue save(TaskQueue queue){
		queue.setCreateTime(DateUtil.now());
		queue.setCurrentTimes(0);
		queue.setStatus(TaskStatus.WAITING);
		if(queue.getTryTimes()==null)
			queue.setTryTimes(taskPluginConfig.getTryTimes());
		return super.save(queue);
	}
}
