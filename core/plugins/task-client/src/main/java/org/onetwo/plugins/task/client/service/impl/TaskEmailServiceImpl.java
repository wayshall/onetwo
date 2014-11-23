package org.onetwo.plugins.task.client.service.impl;

import java.io.File;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.RandUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.email.JavaMailService;
import org.onetwo.plugins.email.MailInfo;
import org.onetwo.plugins.email.MailTextContextParser;
import org.onetwo.plugins.task.TaskCorePlugin;
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
		
		String baseDir = TaskCorePlugin.getInstance().getConfig().getTaskConfig().getAttachmentDir();
//		String baseDir = EmailPlugin.getInstance().getConfig().getAttachmentDir();
		for(File file : mailInfo.getAttachments()){
			String path = file.getPath();
			if(path.startsWith(baseDir)){
				path = path.substring(baseDir.length());
				email.addAttachment(path);
			}else{
				String fileName = FileUtils.getExtendName(path);
				String ext = FileUtils.getExtendName(path, true);
				fileName = fileName + NiceDate.New().format("yyyyMMddHHmmssSSS")+"-"+RandUtils.randomString(6)+ext;
				FileUtils.copyFileTo(file, baseDir, fileName);
				email.addAttachment(fileName);
			}
		}
		return taskClientServiceImpl.save(email);
	}
	
}
