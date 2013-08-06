package org.example.utils;


public interface MenuInfo {
	public String name = "菜单管理";
	
	public static interface SystemManager {
		public String name = "系统管理";
		
		public static interface UserManager {
			public String name = "用户管理";
			

			public static interface New {
				public String name = "新增用户";
				public boolean pageElement = true;
			}
			public static interface Edit {
				public String name = "编辑用户";
			}
			
		}
		
		public static interface RoleManager {
			public String name = "角色管理";
			

			public static interface New {
				public String name = "新增角色";
			}
			public static interface Edit {
				public String name = "编辑角色";
			}
			
		}
	}


}
