package org.onetwo.app.taskservice.actor;

import java.util.List;

import org.onetwo.app.taskservice.service.TaskExecuteListener;
import org.onetwo.app.taskservice.service.TaskListenerManager;
import org.onetwo.app.taskservice.service.impl.TaskQueueServiceImpl;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.task.entity.TaskExecLog;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskConstant.TaskExecResult;
import org.onetwo.plugins.task.utils.TaskResult;
import org.onetwo.plugins.task.utils.TaskUtils;
import org.slf4j.Logger;

import akka.actor.UntypedActor;

public class TaskExecActor extends UntypedActor {
	
//	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private final TaskListenerManager taskListenerManager;

	private TaskQueueServiceImpl taskQueueService;
	
	
	public TaskExecActor(TaskListenerManager taskListenerManager, TaskQueueServiceImpl taskQueueService) {
		super();
		this.taskListenerManager = taskListenerManager;
		this.taskQueueService = taskQueueService;
	}

	@Override
	public void onReceive(Object taskInfo) throws Exception {
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("TaskExecActor receive :\n");
		if(taskInfo instanceof TaskQueue){
			TaskQueue taskQueue = (TaskQueue) taskInfo;
			logMsg.append("task : ").append(taskQueue.getName()).append("\n");
			List<TaskExecuteListener> listeners = taskListenerManager.getTaskExecuteListeners(taskQueue.getTaskType());
			if(LangUtils.isNotEmpty(listeners)){

				for(TaskExecuteListener l : listeners){
					TaskResult taskResult = null;
					
					TaskExecLog log = new TaskExecLog();
					log.setStartTime(DateUtil.now());
					
					taskQueue.markExecuted();
					logMsg.append("该任务是第[").append(taskQueue.getCurrentTimes()).append("]次执行……").append("\n");
					
					log.setExecutor(l.getListenerName());
					String json = TaskUtils.asJsonStringWithMaxLimit(taskQueue.getTask());
					log.setTaskInput(json);
					
					try {
						Object rs = l.execute(taskQueue);
						
						//TODO 可封装成策略接口
						this.taskQueueService.archivedIfNecessary(taskQueue, log, TaskExecResult.SUCCEED);
						
						logMsg.append("listener[").append(l.getClass().getName()).append("] execute succeed.");
						logger.info(logMsg.toString());

						log.setTaskOutput(TaskUtils.asJsonStringWithMaxLimit(rs));
						taskResult = new TaskResult(taskQueue, rs);
						
					} catch (Exception e) {
						logMsg.append("listener[").append(l.getClass().getName()).append("] execute error: ").append(e.getMessage());
						logger.info(logMsg.toString(), e);
						
						if(NullPointerException.class.isInstance(e)){
							String emsg = LangUtils.toString(e, true);
							emsg = TaskUtils.maxLimitString(emsg);
							log.setTaskOutput(emsg);
						}else{
							log.setTaskOutput(e.getMessage());
						}
						//TODO 可封装成策略接口
						this.taskQueueService.archivedIfNecessary(taskQueue, log, TaskExecResult.FAILED);

						taskResult = new TaskResult(taskQueue, e);
					}
					if(taskQueue.isReply()){
						getSender().tell(taskResult, getSelf());
					}
				}
			}else{
				logMsg.append("no listener found for task type: " + taskQueue.getTaskType());
				logger.info(logMsg.toString());
			}
			
		}else{
			logMsg.append("unknown task : ").append(taskInfo);
			logger.info(logMsg.toString());
		}
	}

}
