package org.onetwo.plugins.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.onetwo.plugins.task.utils.TaskType;

@Entity
@Table(name="TASK_EMAIL")
public class TaskEmail extends TaskBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6892549859686221264L;
	private String subject;
	private String content;
	private String attachmentPath;
	@Column(name="IS_HTML")
	private boolean html;
	
	public TaskEmail(){
		setType(TaskType.EMAIL.toString());
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public boolean isHtml() {
		return html;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}
	
}
