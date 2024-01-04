package org.onetwo.boot.module.mail;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;

import org.onetwo.boot.module.mail.SendMailMessage.Attachment;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class JavaMailService {
//	private final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String mailFrom;

	public void send(String title, String[] toAddressList, String content) {
		SendMailMessage sendMailInfo = new SendMailMessage();
		sendMailInfo.setTitle(title);
		sendMailInfo.setTo(toAddressList);
		sendMailInfo.setContent(content);
		sendText(sendMailInfo);
	}
	
	public void sendText(SendMailMessage sendMailInfo) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(sendMailInfo.getTo());
        message.setSubject(sendMailInfo.getTitle());
        message.setText(sendMailInfo.getContent());
        message.setCc(sendMailInfo.getCc());
        message.setBcc(sendMailInfo.getBcc());
		javaMailSender.send(message);
	}
	
	public void send(String title, String[] toAddressList, String content, String attachmentFilename, InputStreamSource inputStreamSource) {
		SendMailMessage message = new SendMailMessage();
        message.setTo(toAddressList);
        message.setTitle(title);
        message.setContent(content);
        message.addAttachment(attachmentFilename, inputStreamSource);
        send(message);
	}
	
	public void send(SendMailMessage sendMailInfo) {
		if (!sendMailInfo.hasAttachment()) {
			sendText(sendMailInfo);
			return ;
		}
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailFrom);
			helper.setSubject(sendMailInfo.getTitle());
			helper.setTo(sendMailInfo.getTo());
			helper.setText(sendMailInfo.getContent());
			
			for (Attachment attach : sendMailInfo.getAttachments()) {
				String fileName = FileUtils.getFileNameWithoutExt(attach.getFileName());
				String attachName = MimeUtility.encodeText(fileName, "utf-8", "B") + FileUtils.getExtendName(attach.getFileName(), true);
				helper.addAttachment(attachName, attach.getInputStreamSource());
			}
//			helper.addAttachment(attachName, new ByteArrayResource(output.toByteArray()), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		} catch (MessagingException e) {
			throw new BaseException("create MimeMessageHelper error: " + e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			throw new BaseException("encode email attachment file name error: " + e.getMessage(), e);
		}

		javaMailSender.send(message);
	}

}
