package org.onetwo.app.taskserver.service;

import javax.annotation.Resource;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.email.EmailConfig;
import org.onetwo.plugins.email.EmailPlugin;
import org.onetwo.plugins.email.JavaMailService;
import org.onetwo.plugins.email.MailInfo;
import org.onetwo.plugins.task.entity.TaskEmail;
import org.onetwo.plugins.task.entity.TaskExecLog;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.onetwo.plugins.task.utils.TaskConstant.TaskExecResult;
import org.onetwo.plugins.task.utils.TaskType;
import org.onetwo.plugins.task.utils.TaskUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class SendEmailServiceImpl implements TaskExecuteListener, TaskTypeMapper {
	final private Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Resource
	private TaskQueueServiceImpl taskQueueService;
	
	@Resource
	private JavaMailService javaMailService;
	
	private EmailConfig emailConfig = EmailPlugin.getInstance().getConfig();
	
	@Override
	public TaskType[] getListenerMappedTaskTypes() {
		return new TaskType[]{TaskType.EMAIL};
	}

	@Override
	public Object execute(TaskQueue taskQueue) {

		StringBuilder msg = new StringBuilder("开始执行发送邮件任务: ").append(taskQueue.getName()).append("\n");
		
		TaskExecLog log = new TaskExecLog();
		log.setStartTime(DateUtil.now());
		
		Assert.isInstanceOf(TaskEmail.class, taskQueue.getTask());
		TaskEmail email = (TaskEmail) taskQueue.getTask();
		TaskExecResult rs = null;
		try {
			taskQueue.markExecuted();
			msg.append("该任务是第[").append(taskQueue.getCurrentTimes()).append("]次执行……").append("\n");
			log.setExecutor(BaseSiteConfig.getInstance().getAppCode());
			log.setTaskInput(JsonMapper.IGNORE_EMPTY.toJson(email));
			
			MailInfo mailInfo = MailInfo.create(emailConfig.getUsername(), email.getToAsArray())
										.cc(email.getCcAsArray())
										.subject(email.getSubject()).content(email.getContent())
										.contentType(email.getContentType())
										.mimeMail(email.isHtml());
			for(String path : email.getAttachmentPathAsArray()){
				String fpath = emailConfig.getAttachmentPath(path);
				mailInfo.addAttachment(FileUtils.newFile(fpath, true));
			}
			this.javaMailService.send(mailInfo);
			
			rs = TaskExecResult.SUCCEED;
			this.taskQueueService.archivedIfNecessary(taskQueue, log, rs);

			msg.append("执行结果: ").append(rs);
			logger.info(msg.toString());
		} catch (Exception e) {
			rs = TaskExecResult.FAILED;
			msg.append("执行结果: ").append(rs);
			logger.error(msg.toString(), e);
			
			log.setTaskOutput(e.getMessage());
			this.taskQueueService.archivedIfNecessary(taskQueue, log, rs);
		} finally{
		}
		return taskQueue;
	}
	

}
