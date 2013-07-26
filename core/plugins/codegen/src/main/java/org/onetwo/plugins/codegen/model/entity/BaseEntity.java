package org.onetwo.plugins.codegen.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.onetwo.common.db.IBaseEntity;

@MappedSuperclass
abstract public class BaseEntity<PK extends Serializable> implements IBaseEntity{
 
	private static final long serialVersionUID = 122579169646461421L;

	protected Date createTime;
	
	protected Date lastUpdateTime;

	@Column(name="CREATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="LAST_UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
	
	