package org.onetwo.boot.module.mail;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.springframework.core.io.InputStreamSource;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SendMailMessage {

	String title;
	/***
	 * 发送目标
	 */
	String[] to;
	String content;
	/****
	 * 抄送
	 */
	String[] cc;
	/***
	 * 密送
	 */
	String[] bcc;
	/***
	 * 附件
	 */
	List<Attachment> attachments;
	
	boolean hasAttachment() {
		return LangUtils.isNotEmpty(attachments);
	}
	
	public void addAttachment(String fileName, InputStreamSource inputStreamSource) {
		Attachment attach = new Attachment(fileName, inputStreamSource);
		addAttachment(attach);
	}
	
	public void addAttachment(Attachment attach) {
		if (attachments==null) {
			attachments = Lists.newArrayList();
		}
		attachments.add(attach);
	}
	
	@Data
	@NoArgsConstructor
	public static class Attachment {
		String fileName;
		InputStreamSource inputStreamSource;
		public Attachment(String fileName, InputStreamSource inputStreamSource) {
			super();
			this.fileName = fileName;
			this.inputStreamSource = inputStreamSource;
		}
	}
	
}
