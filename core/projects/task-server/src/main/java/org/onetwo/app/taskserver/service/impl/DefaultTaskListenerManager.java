package org.onetwo.app.taskserver.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.app.taskserver.service.TaskCompleteListener;
import org.onetwo.app.taskserver.service.TaskExecuteListener;
import org.onetwo.app.taskserver.service.TaskListenerManager;
import org.onetwo.app.taskserver.service.TaskTypeMapper;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.map.ListMap;
import org.onetwo.plugins.task.utils.TaskType;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

public class DefaultTaskListenerManager implements TaskListenerManager, InitializingBean {

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private ListMap<TaskType, TaskExecuteListener> taskExecMapper = ListMap.newListMap(); 
	private ListMap<TaskType, TaskCompleteListener> taskCompleteMapper = ListMap.newListMap(); 

	@Resource
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<TaskTypeMapper> mappers = SpringUtils.getBeans(applicationContext, TaskTypeMapper.class);
		for(TaskTypeMapper mapper : mappers){
			TaskType[] types = mapper.getListenerMappedTaskTypes();
			Assert.notEmpty(types);
			for(TaskType type : types){
				if(TaskExecuteListener.class.isInstance(mapper)){
					addTaskExecuteListener(type, (TaskExecuteListener)mapper);
				}
				if(TaskCompleteListener.class.isInstance(mapper)){
					addTaskCompleteListener(type, (TaskCompleteListener)mapper);
				}
			}
		}
	}

	@Override
	public TaskListenerManager addTaskExecuteListener(TaskType type, TaskExecuteListener taskExecutor){
		logger.info("add TaskExecuteListener: [tasktype={}, listener={}]", type, taskExecutor);
		this.taskExecMapper.putElement(type, taskExecutor);
		return this;
	}
	
	@Override
	public List<TaskExecuteListener> getTaskExecuteListeners(TaskType type){
		return taskExecMapper.get(type);
	}
	
	@Override
	public TaskListenerManager addTaskCompleteListener(TaskType type, TaskCompleteListener taskCompletor){
		logger.info("add TaskCompleteListener: [tasktype={}, listener={}]", type, taskCompletor);
		this.taskCompleteMapper.putElement(type, taskCompletor);
		return this;
	}
	
	@Override
	public List<TaskCompleteListener> getTaskCompleteListeners(TaskType type){
		return taskCompleteMapper.get(type);
	}

}
