package org.onetwo.app.taskserver.service;

import java.io.File;
import java.io.InputStream;

import javax.annotation.Resource;

import org.onetwo.app.taskserver.TaskServerConfig;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.io.SmbInputStreamSource;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.plugins.email.EmailConfig;
import org.onetwo.plugins.email.EmailPlugin;
import org.onetwo.plugins.email.JavaMailService;
import org.onetwo.plugins.email.MailInfo;
import org.onetwo.plugins.task.TaskPluginConfig;
import org.onetwo.plugins.task.entity.TaskEmail;
import org.onetwo.plugins.task.entity.TaskExecLog;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.onetwo.plugins.task.utils.TaskConstant.TaskExecResult;
import org.onetwo.plugins.task.utils.TaskType;
import org.slf4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
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
	
	@Resource
	private TaskPluginConfig taskPluginConfig;

	@Resource
	private TaskServerConfig taskServerConfig;
	
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
			log.setExecutor(taskServerConfig.getAppCode());
			log.setTaskInput(JsonMapper.IGNORE_EMPTY.toJson(email));
			
			MailInfo mailInfo = MailInfo.create(emailConfig.getUsername(), email.getToAsArray())
										.cc(email.getCcAsArray())
										.subject(email.getSubject()).content(email.getContent())
										.emailTextType(email.getContentType())
										.mimeMail(email.isHtml());
			for(String path : email.getAttachmentPathAsArray()){
				String fpath = taskPluginConfig.getTaskConfig().getAttachmentPath(path);
				logger.info("add attachement: {}", fpath);
				/*String attachName = FileUtils.getFileName(path);
				InputStreamSource attachment = createAttachmentInputStreamSource(fpath);
				mailInfo.addAttachmentInputStreamSource(attachName, attachment);*/
				File file = copyToLocalIfSmbFile(fpath);
				mailInfo.addAttachment(file);
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
	
	protected InputStreamSource createAttachmentInputStreamSource(String path){
		InputStream in = FileUtils.newInputStream(path);
		if(in==null){
			throw new BaseException("create attachement inputstream error, path: " + path);
		}
		InputStreamSource iss = null;
		if(FileUtils.isSmbPath(path)){
			iss = new SmbInputStreamSource(path);
		}else{
			iss = new FileSystemResource(path);
		}
		return iss;
	}
	
	protected File copyToLocalIfSmbFile(String path){
		if(FileUtils.isSmbPath(path)){
			File file = FileUtils.copyFileToDir(FileUtils.newSmbFile(path), taskServerConfig.getLocalAttachmentDir());
			file.deleteOnExit();
			return file;
		}else{
			return new File(path);
		}
	}
	

}
