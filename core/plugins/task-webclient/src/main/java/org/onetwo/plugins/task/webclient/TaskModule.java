package org.onetwo.plugins.task.webclient;

import org.onetwo.plugins.permission.entity.PermissionType;

public interface TaskModule {
	String name = "任务管理";

	public static interface Queue {
		String name = "队列";

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

		public static interface ExeLog {
			String name = "执行明细";
			PermissionType permissionType = PermissionType.FUNCTION;
		}
	}

	public static interface Archived {
		String name = "归档";

		public static interface List {
			String name = "列表";
		}

		public static interface ReQueue {
			String name = "重新放入队列";
			PermissionType permissionType = PermissionType.FUNCTION;
		}

	}

}
