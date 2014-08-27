package org.onetwo.plugins.task.vo;

import java.io.Serializable;
import java.util.Date;

import org.onetwo.plugins.task.utils.TaskConstant.YesNo;
import org.springframework.format.annotation.DateTimeFormat;

public class TaskEmailVo implements Serializable {
	private Long id;
	private String name;
	private String bizTag;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date planTime;
	private boolean html;
	private String subject;
	private String content;
	
	public boolean htmlChecked(YesNo yesNo){
		return html==yesNo.getBoolean();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBizTag() {
		return bizTag;
	}
	public void setBizTag(String bizTag) {
		this.bizTag = bizTag;
	}
	public Date getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	public boolean isHtml() {
		return html;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
