package org.onetwo.boot.mq;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.jackson.JsonMapper;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
final public class MQUtils {
	public static final String CONVERTER_JACKSON2 = "jackson2";

	/****
	 * __jfish_jackson2_type__
	 */
	public static final String TYPE_ID_PROPERTY_NAME = "__" + BootJFishConfig.ZIFISH_CONFIG_PREFIX + "_jackson2_type__";
	
	public static final JsonMapper TYPING_JSON_MAPPER = JsonMapper.ignoreNull().enableTyping();
	
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
