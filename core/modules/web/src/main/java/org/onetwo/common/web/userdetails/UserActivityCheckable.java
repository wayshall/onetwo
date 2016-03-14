package org.onetwo.common.web.userdetails;

import java.util.Date;

/****
 * 是否检查登陆用户的最后活动时间，如果验证注解的checkTimeout属性为true，则验证器会检查userDetail对象是否实现了本接口，如果是则进行最后活动时间检测。
 * @author way
 *
 */
public interface UserActivityCheckable extends UserDetail {

	public void setLastActivityTime(Date lastActivityTime);
	public Date getLastActivityTime();

	public Date getLastSynchronizedTime();
	public void setLastSynchronizedTime(Date lastSynchronizedTime);
}
