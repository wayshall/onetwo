package org.onetwo.plugins.task.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Size;

import org.onetwo.plugins.task.utils.TaskUtils;

@Entity
@Table(name="TASK_BIZ_TAG")
@TableGenerator(table=TaskUtils.SEQ_TABLE_NAME, name="TaskBizTagGenerator", pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_TASK_BIZ_TAG", allocationSize=50, initialValue=1000)
public class TaskBizTag implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="TaskBizTagGenerator") 
	private Long id;
	@Size(max=50)
	private String name;
	
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
}
