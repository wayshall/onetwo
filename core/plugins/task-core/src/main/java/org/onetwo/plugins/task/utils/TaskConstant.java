package org.onetwo.plugins.task.utils;

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
		EXECUTING("排队执行中"),
		
		final private String name;

		private TaskStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
	}

	/***
	 * 
FAILED - 执行失败
SUCCEED- 执行成功
	 * @author Administrator
	 *
	 */
	public static enum TaskExecResult {
		FAILED("执行失败"),
		SUCCEED("执行成功");
		
		final private String name;

		private TaskExecResult(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
	}
	
	public static enum YesNo {
		NO("否"),
		YES("是");
		
		private final String name;

		private YesNo(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
		public int getValue(){
			return ordinal();
		}
		
		public boolean getBoolean(){
			return ordinal()==1;
		}
		
	}
	
	private TaskConstant(){
	}

}
