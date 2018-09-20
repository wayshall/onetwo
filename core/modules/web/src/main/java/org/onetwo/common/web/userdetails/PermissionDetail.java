package org.onetwo.common.web.userdetails;

import java.util.List;

/****
 * 以前自己实现的一套的sso的遗留接口
 * 已废弃
 * 
 * @author wayshall
 *
 */
@Deprecated
public interface PermissionDetail {

//	public void  setPermissions(List<String> permissions);

	public List<String> getPermissions();
}
