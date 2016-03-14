package org.onetwo.common.db;

import java.util.Date;


@SuppressWarnings("serial")
abstract public class AbstractTimeRecordableEntity implements TimeRecordableEntity {

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
