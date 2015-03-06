package org.onetwo.plugins.admin;

import org.onetwo.plugins.permission.entity.PermissionType;

public interface DataModule {
	String name = "数据管理";



	public static interface ImportData {
		String name = "导入项目字典数据";
	}
	
	public static interface DictModule {
		String name = "字典管理";
		public static interface List {
			String name = "列表";
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
		
	}
	

}
