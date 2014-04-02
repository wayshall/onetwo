package com.qyscard.task.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.qyscard.task.utils.TaskUtils;

@Entity
@Table(name="TASK_EMAIL")
@TableGenerator(table=TaskUtils.SEQ_TABLE_NAME, name="TaskEmailEntityGenerator", pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_ADMIN_USER", allocationSize=50, initialValue=1000)
public class TaskEmailEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1767494940540574004L;
	private Long id;
	private String title;
	private String content;
	private String attachPaths;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TaskEmailEntityGenerator") 
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
}
