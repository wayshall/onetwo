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
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.onetwo.plugins.task.utils.TaskConstant.TaskExecResult;
import org.onetwo.plugins.task.utils.TaskUtils;

@Entity
@Table(name="TASK_EXEC_LOG")
@TableGenerator(table=TaskUtils.SEQ_TABLE_NAME, name="TaskExecLogEntityGenerator", pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_ADMIN_USER", allocationSize=50, initialValue=1000)
public class TaskExecLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4528661067353586519L;
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TaskExecLogEntityGenerator") 
	@Column(name="ID")
	private Long id;
	@Enumerated(EnumType.STRING)
	private TaskExecResult result;
	private String executor;
	
	private Date startTime;
	private Date endTime;

	private String taskInput;
	private String taskOutput;
	
	private Long taskQueueId;
	
	private Integer execIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TaskExecResult getResult() {
		return result;
	}

	public void setResult(TaskExecResult result) {
		this.result = result;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTaskInput() {
		return taskInput;
	}

	public void setTaskInput(String taskInput) {
		this.taskInput = taskInput;
	}

	public String getTaskOutput() {
		return taskOutput;
	}

	public void setTaskOutput(String taskOutput) {
		this.taskOutput = taskOutput;
	}

	public Long getTaskQueueId() {
		return taskQueueId;
	}

	public void setTaskQueueId(Long taskQueueId) {
		this.taskQueueId = taskQueueId;
	}

	public Integer getExecIndex() {
		return execIndex;
	}

	public void setExecIndex(Integer execIndex) {
		this.execIndex = execIndex;
	}

}
