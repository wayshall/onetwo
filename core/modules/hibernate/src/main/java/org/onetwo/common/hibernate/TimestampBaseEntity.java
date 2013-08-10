package org.onetwo.common.hibernate;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import org.onetwo.common.db.IBaseEntity;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class TimestampBaseEntity implements IBaseEntity {
	
	private Date createTime;
	private Date lastUpdateTime;
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	

}
