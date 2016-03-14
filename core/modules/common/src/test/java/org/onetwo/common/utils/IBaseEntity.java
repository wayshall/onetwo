package org.onetwo.common.utils;

import java.util.Date;

public interface IBaseEntity {

	public Date getCreateTime();

	public void setCreateTime(Date createTime);

	public Date getLastUpdateTime();

	public void setLastUpdateTime(Date lastUpdateTime);
}
