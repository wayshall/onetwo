package org.onetwo.plugins.akka.task.utils;

final public class AkkaTaskConstant {
	

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

	private AkkaTaskConstant(){
	}
}
