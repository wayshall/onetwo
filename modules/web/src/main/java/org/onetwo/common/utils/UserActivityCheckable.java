package org.onetwo.common.utils;

import java.util.Date;

public interface UserActivityCheckable extends UserDetail {

	public void setLastActivityTime(Date lastActivityTime);
	public Date getLastActivityTime();

	public Date getLastSynchronizedTime();
	public void setLastSynchronizedTime(Date lastSynchronizedTime);
}
