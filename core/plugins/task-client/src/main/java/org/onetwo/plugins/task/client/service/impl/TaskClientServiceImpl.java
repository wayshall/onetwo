package org.onetwo.plugins.task.client.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.email.ContentType;
import org.onetwo.plugins.task.entity.TaskBase;
import org.onetwo.plugins.task.entity.TaskBizTag;
import org.onetwo.plugins.task.entity.TaskEmail;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.onetwo.plugins.task.vo.TaskEmailVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskClientServiceImpl {
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Resource
	private TaskQueueServiceImpl taskQueueService;
	
	public TaskQueue load(Long id){
		return taskQueueService.load(id);
	}
	
	public TaskBizTag loadOrSave(String bizTag){
		if(StringUtils.isBlank(bizTag))
			return null;
		
		TaskBizTag tag = baseEntityManager.findOne(TaskBizTag.class, "name", bizTag);
		if(tag==null){
			tag = new TaskBizTag();
			tag.setName(bizTag);
			baseEntityManager.save(tag);
		}
		return tag;
	}
	
	public TaskQueue save(TaskEmailVo taskVo){
		TaskQueue taskQueue = null;
		if(taskVo.getId()!=null)
			taskQueue = taskQueueService.load(taskVo.getId());
		TaskBizTag tag = loadOrSave(taskVo.getBizTag());
		if(taskQueue==null){
			taskQueue = new TaskQueue();
			if(taskVo.getPlanTime()!=null){
				taskQueue.setPlanTime(taskVo.getPlanTime());
			}else{
				taskQueue.setPlanTime(new Date());
			}
			
			TaskEmail email = new TaskEmail();
			email.setContentType(ContentType.STATIC_TEXT);
			HibernateUtils.copyIgnoreRelationsAndFields(taskVo, email, "id");
			email.setTag(tag);
			
			taskQueue.setTask(email);
			baseEntityManager.save(taskQueue.getTask());
			return taskQueueService.save(taskQueue);
		}else{
			TaskQueue dbTaskQueue = load(taskQueue.getId());
			taskQueue.setPlanTime(taskVo.getPlanTime());

			HibernateUtils.copyIgnoreRelationsAndFields(taskVo, taskQueue.getTask(), "id");
			taskQueue.getTask().setTag(tag);
			
			return dbTaskQueue;
		}
	}
	
	public TaskQueue addTaskToQueue(TaskBase task, Date planTime){
		TaskQueue queue = new TaskQueue();
//		queue.setName(taskClientConfig.getQueueNamePrefix()+StringUtils.emptyIfNull(task.getName()));
		queue.setTask(task);
		queue.setPlanTime(planTime);
		taskQueueService.save(queue);
		return queue;
	}
	

}
