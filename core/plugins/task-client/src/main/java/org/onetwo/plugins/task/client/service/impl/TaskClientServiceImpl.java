package org.onetwo.plugins.task.client.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.task.client.TaskClientConfig;
import org.onetwo.plugins.task.entity.TaskBase;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskConstant.TaskStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskClientServiceImpl {
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Resource
	private TaskClientConfig taskClientConfig;
	
	public void addTaskToQueue(TaskBase task, Date planTime){
		TaskQueue queue = new TaskQueue();
		queue.setName(taskClientConfig.getQueueNamePrefix()+StringUtils.emptyIfNull(task.getName()));
		queue.setTask(task);
		queue.setPlanTime(planTime);
		addQueue(queue);
	}
	
	/***
	 * 添加到数据库队列
	 * @param queue
	 */
	public void addQueue(TaskQueue queue){
		queue.setCreateTime(DateUtil.now());
		queue.setCurrentTimes(0);
		queue.setStatus(TaskStatus.WAITING);
		if(queue.getTryTimes()==null)
			queue.setTryTimes(taskClientConfig.getTryTimes());
		this.baseEntityManager.save(queue);
	}

}
