package org.onetwo.common.db;

import java.io.Serializable;
import java.util.Date;


public interface TimeRecordableEntity extends Serializable {

	public Date getCreateAt();
	
	public void setCreateAt(Date createTime);

	public Date getUpdateAt();

	public void setUpdateAt(Date lastUpdateTime);

}
