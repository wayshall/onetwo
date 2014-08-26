package org.onetwo.plugins.task.client;

import org.onetwo.plugins.permission.entity.PermissionType;

public interface TaskModule {
	String name = "任务管理";
	
	public static interface QueueList {
		String name = "任务队列";
	}

	public static interface New {
		String name = "新增";
		PermissionType permissionType = PermissionType.FUNCTION;
	}

	public static interface Edit {
		String name = "编辑";
		PermissionType permissionType = PermissionType.FUNCTION;
	}

}
