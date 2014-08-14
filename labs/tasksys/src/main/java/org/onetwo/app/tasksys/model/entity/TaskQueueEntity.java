package org.onetwo.app.tasksys.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.onetwo.app.tasksys.model.ReplyTaskData;
import org.onetwo.app.tasksys.model.TaskResult;
import org.onetwo.app.tasksys.model.TaskType;
import org.onetwo.app.tasksys.utils.TaskConstant.TaskStatus;
import org.onetwo.app.tasksys.utils.TaskUtils;

@Entity
@Table(name="TASK_QUEUE")
@TableGenerator(table=TaskUtils.SEQ_TABLE_NAME, name="TaskQueueEntityGenerator", pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_ADMIN_USER", allocationSize=50, initialValue=1000)
public class TaskQueueEntity implements Serializable, ReplyTaskData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -935205537821315792L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TaskQueueEntityGenerator") 
	@Column(name="ID")
	private Long id;
	private String name;
	
	@Enumerated(EnumType.STRING)
	private TaskType type;
	
	private String bizType;
	
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	private Integer currentTimes;
	private Integer tryTimes;
	private Date planTime;
	private Date createTime;

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

	
	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Integer getCurrentTimes() {
		return currentTimes;
	}

	public void setCurrentTimes(Integer currentTimes) {
		this.currentTimes = currentTimes;
	}

	public Integer getTryTimes() {
		return tryTimes;
	}

	public void setTryTimes(Integer tryTimes) {
		this.tryTimes = tryTimes;
	}

	public Date getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	@Override
	public void setResult(TaskResult result) {
		System.out.println("set result..");
	}

	@Override
	public TaskResult getResult() {
		return null;
	}

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}

}
