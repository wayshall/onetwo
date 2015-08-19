package org.onetwo.common.db;

import java.io.Serializable;
import java.util.Date;


public interface IBaseEntity extends Serializable {

	public Date getCreateTime();
	
	public void setCreateTime(Date createTime);

	public Date getLastUpdateTime();

	public void setLastUpdateTime(Date lastUpdateTime);

}
