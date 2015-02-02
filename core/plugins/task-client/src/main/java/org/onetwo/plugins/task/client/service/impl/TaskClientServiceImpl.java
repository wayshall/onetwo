package org.onetwo.plugins.task.client.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.email.EmailTextType;
import org.onetwo.plugins.task.TaskCoreConfig;
import org.onetwo.plugins.task.entity.TaskBase;
import org.onetwo.plugins.task.entity.TaskBizTag;
import org.onetwo.plugins.task.entity.TaskEmail;
import org.onetwo.plugins.task.entity.TaskExecLog;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.entity.TaskQueueArchived;
import org.onetwo.plugins.task.utils.TaskConstant.QueueSourceTag;
import org.onetwo.plugins.task.utils.TaskConstant.TaskStatus;
import org.onetwo.plugins.task.vo.TaskEmailVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskClientServiceImpl {
	

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
	

	public TaskQueue load(Long id){
		return baseEntityManager.load(TaskQueue.class, id);
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
			taskQueue = load(taskVo.getId());
		TaskBizTag tag = loadOrSave(taskVo.getBizTag());
		if(taskQueue==null){
			taskQueue = new TaskQueue();
			if(taskVo.getPlanTime()!=null){
				taskQueue.setPlanTime(taskVo.getPlanTime());
			}else{
				taskQueue.setPlanTime(new Date());
			}
			
			TaskEmail email = new TaskEmail();
			email.setContentType(EmailTextType.STATIC_TEXT);
			HibernateUtils.copyIgnoreRelationsAndFields(taskVo, email, "id");
			email.setTag(tag);
			email.setName(taskVo.getSubject());
			
			taskQueue.setTask(email);
			baseEntityManager.save(taskQueue.getTask());
			return baseEntityManager.save(taskQueue);
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
		baseEntityManager.save(queue);
		return queue;
	}
	
	

}
