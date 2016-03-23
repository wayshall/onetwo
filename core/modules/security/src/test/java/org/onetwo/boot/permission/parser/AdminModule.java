package org.onetwo.boot.permission.parser;

import org.onetwo.ext.permission.api.PermissionType;

public interface AdminModule {
	String name = "全局用户角色权限管理";

	public static interface AppRole {
		String name = "角色管理";
		int sort =1;
		public static interface List {
			String name = "列表";
			int sort =1;
		}

		public static interface New {
			String name = "新增";
			PermissionType permissionType = PermissionType.FUNCTION;
		}

		public static interface Edit {
			String name = "编辑";
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

	public static interface AppUser {
		String name = "用户管理";
		int sort =2;
		public static interface List {
			String name = "列表";
			int sort =1;
		}

		public static interface New {
			String name = "新增";
			PermissionType permissionType = PermissionType.FUNCTION;
		}

		public static interface Edit {
			String name = "编辑";
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
}
