package org.onetwo.common.sso;

import java.util.Date;

public interface UserActivityTimeHandler {

	/*********
	 * update to db
	 * @param token
	 * @param time
	 */
	public void updateUserLastActivityTime(String token, Date time);
	
	/*********
	 * get from db
	 * @param token
	 * @return
	 */
	public SSOLastActivityStatus getUserLastActivityStatus(String token);
	
}
