package org.onetwo.plugins.task.client.service.impl;

import java.io.File;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.RandUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.email.JavaMailService;
import org.onetwo.plugins.email.MailInfo;
import org.onetwo.plugins.email.MailTextContextParser;
import org.onetwo.plugins.task.TaskCorePlugin;
import org.onetwo.plugins.task.entity.TaskConfig;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskUtils;
import org.onetwo.plugins.task.vo.TaskEmailVo;

public class TaskEmailServiceImpl implements JavaMailService {
	
	@Resource
	private TaskClientServiceImpl taskClientServiceImpl;
	
	@Resource
	private MailTextContextParser mailTextContextParser;

	@Override
	public void send(MailInfo mailInfo) throws MessagingException {
		save(mailInfo);
	}
	

	public TaskQueue save(MailInfo mailInfo){
		TaskConfig taskConfig = TaskCorePlugin.getInstance().getConfig().getTaskConfig();
		
		TaskEmailVo email = new TaskEmailVo();
		email.setSubject(mailInfo.getSubject());
		email.setBizTag(mailInfo.getBizTag());
		email.setCcAddress(StringUtils.join(mailInfo.getCc(), TaskUtils.EMAIL_SPLITTER));
//		email.setContent(mailInfo.getContent());
//		email.setPlanTime(DateUtil.now());
		email.setToAddress(StringUtils.join(mailInfo.getTo(), TaskUtils.EMAIL_SPLITTER));
		email.setHtml(mailInfo.isMimeMail());
		
		String content = this.mailTextContextParser.parseContent(mailInfo);
		email.setContent(content);
		
//		String baseDir = EmailPlugin.getInstance().getConfig().getAttachmentDir();
		for(File file : mailInfo.getAttachments()){
			if(!file.exists()){
				throw new BaseException("file not found: " +file.getPath());
			}
			String path = FileUtils.replaceBackSlashToSlash(file.getPath());
			
			if(taskConfig.isRestoreEmailAttachment()){
				String baseDir = taskConfig.getEmailAttachmentDir();
				Assert.hasText(baseDir, "email attachment dir must be config!");
				NiceDate date = NiceDate.New();
				String fileName = FileUtils.getFileNameWithoutExt(path);
				String ext = FileUtils.getExtendName(path, true);
				fileName = fileName + NiceDate.New().format("HHmmssSSS")+"-"+RandUtils.randomString(6)+ext;
				
				String subPath = date.format("yyyy-MM-dd");
				subPath += "/" + fileName;
				FileUtils.copyFileTo(file, baseDir, subPath);
				email.addAttachment(subPath);
				
			}else{
				//只保存部分路径
				String baseDir = taskConfig.getEmailAttachmentDir();
				path = path.substring(baseDir.length());
				email.addAttachment(path);
			}
		}
		return taskClientServiceImpl.save(email);
	}
	
}
