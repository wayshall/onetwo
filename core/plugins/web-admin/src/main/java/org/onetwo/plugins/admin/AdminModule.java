package org.onetwo.plugins.admin;

import org.onetwo.ext.permission.api.PermissionType;

public interface AdminModule {
	String name = "用户角色权限管理";

	public static interface AdminRoleMgr {
		String name = "角色管理";
		int sort =1;
/*		public static interface List {
			String name = "角色列表";
			int sort =1;
		}
*/
		public static interface Create {
			String name = "新增";
			PermissionType permissionType = PermissionType.FUNCTION;
		}

		public static interface Update {
			String name = "更新";
			PermissionType permissionType = PermissionType.FUNCTION;
		}

		public static interface Delete {
			String name = "删除";
			PermissionType permissionType = PermissionType.FUNCTION;
		}
		
		public static interface AssignPermission {
			String name = "分配权限";
			PermissionType permissionType = PermissionType.FUNCTION;
		}
	}

	public static interface AdminUserMgr {
		String name = "用户管理";
		int sort =2;
		/*public static interface List {
			String name = "用户列表";
			int sort =1;
		}*/

		public static interface Create {
			String name = "新增";
			PermissionType permissionType = PermissionType.FUNCTION;
		}

		public static interface Update {
			String name = "更新";
			PermissionType permissionType = PermissionType.FUNCTION;
		}
		
		public static interface Delete {
			String name = "删除";
			PermissionType permissionType = PermissionType.FUNCTION;
		}

		public static interface AssignRole {
			String name = "分配角色";
			PermissionType permissionType = PermissionType.FUNCTION;
		}
	}
	

	public interface DictMgr {
		String name = "字典配置管理";
	}
}
