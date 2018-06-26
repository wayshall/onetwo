package org.onetwo.boot.mq;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
final public class MQUtils {
	public static final MQResult DEFAULT_SUSPEND = new MQResult("SUSPEND");
	
	@Data
	public static class MQResult {
		final String state;

		public MQResult(String state) {
			super();
			this.state = state;
		}
	}
	
	public static boolean isSuspendResult(Object res){
		return DEFAULT_SUSPEND.equals(res);
	}
	
	private MQUtils(){
	}
}
