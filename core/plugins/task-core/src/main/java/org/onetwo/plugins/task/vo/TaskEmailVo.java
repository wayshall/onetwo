package org.onetwo.plugins.task.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.task.utils.TaskConstant.YesNo;
import org.onetwo.plugins.task.utils.TaskUtils;
import org.springframework.format.annotation.DateTimeFormat;

public class TaskEmailVo implements Serializable {
	private Long id;
//	private String name;
	private String bizTag;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date planTime;
	private boolean html;
	@NotBlank
	private String subject;
	@NotBlank
	private String content;

	private String ccAddress;
	@NotBlank
	private String toAddress;
	private List<String> attachmentPath;
	
	public boolean htmlChecked(YesNo yesNo){
		return html==yesNo.getBoolean();
	}
	
	public void addAttachment(String path){
		if(attachmentPath==null){
			attachmentPath = LangUtils.newArrayList();
		}
		attachmentPath.add(path);
	}
	
	
	public String getAttachmentPath() {
		if(LangUtils.isEmpty(attachmentPath))
			return "";
		return GuavaUtils.join(attachmentPath, TaskUtils.ATTACHMENT_PATH_SPLITTER);
	}

	/*public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}*/
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

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
}
