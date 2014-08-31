package org.onetwo.plugins.email;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;

public class MailInfo implements Serializable{
	
	public static enum ContentType {
		StaticText,
		TemplatePath,
		Template
	}
	
	public static MailInfo create(String from, String...to){
		MailInfo m = new MailInfo();
		m.from(from);
		m.to(to);
		return m;
	}

	private String from;

	private String replyTo;

	private String[] to;

	private String[] cc;

	private String[] bcc;

	private Date sentDate;

	private String subject;

	private String content;

	private ContentType contentType = ContentType.StaticText;
	private boolean mimeMail;
	
	private JFishList<File> attachments = JFishList.create();
	
	private Map<String, Object> templateContext;

	public String getFrom() {
		return from;
	}

	public MailInfo from(String from) {
		this.from = from;
		return this;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public MailInfo replyTo(String replyTo) {
		this.replyTo = replyTo;
		return this;
	}

	public String[] getTo() {
		return to;
	}

	public MailInfo to(String... to) {
		this.to = to;
		return this;
	}

	public String[] getCc() {
		return cc;
	}

	public MailInfo cc(String... cc) {
		this.cc = cc;
		return this;
	}

	public String[] getBcc() {
		return bcc;
	}

	public MailInfo bcc(String... bcc) {
		this.bcc = bcc;
		return this;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public MailInfo sentDate(Date sentDate) {
		this.sentDate = sentDate;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public MailInfo subject(String subject) {
		this.subject = subject;
		return this;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public MailInfo contentType(ContentType contentType) {
		this.contentType = contentType;
		return this;
	}

	public File[] getAttachments() {
		return attachments.toArray(new File[0]);
	}

	public MailInfo addAttachment(File attachment) {
		if(attachment==null)
			return this;
		attachments.add(attachment);
		this.mimeMail = true;
		return this;
	}

	public MailInfo attachments(File... attachments) {
		if(LangUtils.isEmpty(attachments))
			return this;
		this.attachments  = JFishList.wrapObject(attachments);
		this.mimeMail = true;
		return this;
	}

	public MailInfo attachmentList(List<File> attachments) {
		if(LangUtils.isEmpty(attachments))
			return this;
		this.attachments = JFishList.wrap(attachments);
		this.mimeMail = true;
		return this;
	}

	public String getContent() {
		return content;
	}

	public MailInfo content(String content) {
		this.content = content;
		return this;
	}

	public Map<String, Object> getTemplateContext() {
		return templateContext;
	}

	public MailInfo templateContext(Map<String, Object> templateContext) {
		this.templateContext = templateContext;
		return this;
	}

	public boolean isMimeMail() {
		return mimeMail;
	}

	public MailInfo mimeMail(boolean mimeMail) {
		this.mimeMail = mimeMail;
		return this;
	}
	
}
