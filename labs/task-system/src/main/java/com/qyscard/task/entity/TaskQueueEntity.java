package com.qyscard.task.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.onetwo.app.task.TaskData;
import org.onetwo.app.task.TaskType;

import com.qyscard.task.test.TaskTypeConstant;
import com.qyscard.task.utils.TaskUtils;

@Entity
@Table(name="TASK_QUEUE")
@TableGenerator(table=TaskUtils.SEQ_TABLE_NAME, name="TaskQueueEntityGenerator", pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_ADMIN_USER", allocationSize=50, initialValue=1000)
public class TaskQueueEntity implements Serializable, TaskData {
	
	public static enum BizType {
		EMAIL
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -935205537821315792L;
	private Long id;
	private String name;
	private BizType bizType;
	private String status;
	private Integer currentTimes;
	private Integer tryTimes;
	private Date planTime;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TaskQueueEntityGenerator") 
	@Column(name="ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Transient
	@Override
	public TaskType getType() {
		return TaskTypeConstant.EMAIL;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

}
