package org.onetwo.common.utils;

import java.util.Date;


abstract public class BaseEntity implements IBaseEntity {

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
	
	