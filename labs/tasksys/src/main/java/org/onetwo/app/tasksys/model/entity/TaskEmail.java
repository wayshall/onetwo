package org.onetwo.app.tasksys.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TASK_EMAIL")
public class TaskEmail extends TaskBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6892549859686221264L;
	private String title;
	private String content;
	private String attachPaths;
	private boolean html;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttachPaths() {
		return attachPaths;
	}
	public void setAttachPaths(String attachPaths) {
		this.attachPaths = attachPaths;
	}
	public boolean isHtml() {
		return html;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}
	
}
