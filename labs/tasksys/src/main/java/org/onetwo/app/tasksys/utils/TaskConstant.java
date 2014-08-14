package org.onetwo.app.tasksys.utils;

final public class TaskConstant {
	
	/****
	 * 最近一次状态：
WAITING - 等待中
EXECUTING - 排队执行中
	 * @author Administrator
	 *
	 */
	public static enum TaskStatus {
		WAITING("等待中"),
		EXECUTING("排队执行中");
		
		final private String name;

		private TaskStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
	}
	
	private TaskConstant(){
	}

}
