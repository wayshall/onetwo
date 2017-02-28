package org.onetwo.common.dbm.model.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
abstract public class BaseEntity implements Serializable {
	
	private Date createAt;
	private Date updateAt;
	
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}
