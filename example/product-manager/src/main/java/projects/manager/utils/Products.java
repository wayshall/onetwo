package projects.manager.utils;

import org.onetwo.ext.permission.api.PermissionType;


public interface Products {
	String name = "产品管理系统";
	String appCode = Products.class.getSimpleName();
	
	/*@ProxyMenu(value=AdminModule.class)
	public interface AdminMgr {
	}*/
	
	public interface SystemMgr {
		String name = "系统管理";
		int sort = 1;
		
		public interface UserMgr {
			String name = "账号管理";
			
			public interface Create {
				String name = "新增";
				PermissionType permissionType = PermissionType.FUNCTION;
			}

			public interface Update {
				String name = "更新";
				PermissionType permissionType = PermissionType.FUNCTION;
			}
			
			public interface Delete {
				String name = "删除";
				PermissionType permissionType = PermissionType.FUNCTION;
			}
		}

		public interface ProductMgr {
			String name = "产品管理";
			

			public interface Create {
				String name = "新增";
				PermissionType permissionType = PermissionType.FUNCTION;
			}

			public interface Update {
				String name = "更新";
				PermissionType permissionType = PermissionType.FUNCTION;
			}
			
			public interface Delete {
				String name = "删除";
				PermissionType permissionType = PermissionType.FUNCTION;
			}
		}

		public interface ActiveMgr {
			String name = "激活管理";

			public interface List {
				String name = "激活列表";
			}

			public interface Statis {
				String name = "激活统计";
			}

			public interface Create {
				String name = "新增";
				PermissionType permissionType = PermissionType.FUNCTION;
			}

			public interface Update {
				String name = "更新";
				PermissionType permissionType = PermissionType.FUNCTION;
			}
			
			public interface Delete {
				String name = "删除";
				PermissionType permissionType = PermissionType.FUNCTION;
			}
			
			public interface ActiveIncomeView {
				String name = "激活收入查看";
			}
		}

		public interface IncomeMgr {
			String name = "收益管理";

			public interface List {
				String name = "收益列表";
			}
			
			public interface Statis {
				String name = "收支统计";
			}

			public interface Create {
				String name = "新增";
				PermissionType permissionType = PermissionType.FUNCTION;
			}

			public interface Update {
				String name = "更新";
				PermissionType permissionType = PermissionType.FUNCTION;
			}
			
			public interface Delete {
				String name = "删除";
				PermissionType permissionType = PermissionType.FUNCTION;
			}
			
		}

		public interface UserProfile {
			String name = "修改资料";
		}
	}
	


}
