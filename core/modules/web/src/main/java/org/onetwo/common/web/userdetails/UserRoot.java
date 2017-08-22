package org.onetwo.common.web.userdetails;
/**
 * @author wayshall
 * <br/>
 */
public interface UserRoot {
	public static final long ROOT_USER_ID = 1;

	/***
	 * 是否root管理员
	 * @return
	 */
	public boolean isSystemRootUser();
}
