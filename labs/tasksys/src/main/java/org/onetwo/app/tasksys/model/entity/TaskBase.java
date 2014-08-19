package org.onetwo.app.tasksys.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.onetwo.app.tasksys.utils.TaskUtils;

@Entity
@Table(name="TASK_BASE")
@Inheritance(strategy=InheritanceType.JOINED)
@TableGenerator(table=TaskUtils.SEQ_TABLE_NAME, name="TaskBaseEntityGenerator", pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_TASK_BASE", allocationSize=50, initialValue=1000)
public class TaskBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5996910489379379998L;
	private Long id;
	private String name;
	private String type;
	private String bizType;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TaskBaseEntityGenerator") 
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
}
