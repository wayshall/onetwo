package org.onetwo.plugins.task.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.onetwo.plugins.task.utils.TaskData;
import org.onetwo.plugins.task.utils.TaskType;
import org.onetwo.plugins.task.utils.TaskUtils;
import org.onetwo.plugins.task.utils.TaskConstant.TaskStatus;

@Entity
@Table(name="TASK_QUEUE")
@TableGenerator(table=TaskUtils.SEQ_TABLE_NAME, name="TaskQueueEntityGenerator", pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_ADMIN_USER", allocationSize=50, initialValue=1000)
public class TaskQueue implements Serializable, TaskData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -935205537821315792L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TaskQueueEntityGenerator") 
	@Column(name="ID")
	private Long id;
	private String name;
	
	private String type;
	
	private String bizType;
	
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	private Integer currentTimes;
	private Integer tryTimes;
	private Date planTime;
	private Date createTime;
	private String executor;
	private Date lastExecTime;
	
	@ManyToOne
	@JoinColumn(name="task_id")
	private TaskBase task;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public TaskBase getTask() {
		return task;
	}

	public void setTask(TaskBase task) {
		this.task = task;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.type(type);
	}

	public Date getLastExecTime() {
		return lastExecTime;
	}

	public void setLastExecTime(Date lastExecTime) {
		this.lastExecTime = lastExecTime;
	}

	@Override
	public boolean isReply() {
		return false;
	}

}
