package org.onetwo.common.web.s2;

import java.util.Map;

/****
 * ajax action的实现接口
 * 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public interface AjaxAction {
 
	public static final String MESSAGE_KEY = "message";
	public static final String MESSAGE_CODE_KEY = "message_code";

	public static final String CALLBACK_KEY = ":callback";
	public static final String INCLUDE_FIELDS = ":includeFields";
	public static final String EXCLUDE_FIELDS = ":excludeFields";
	
	public static final Integer RESULT_SUCCEED = 1;
	public static final Integer RESULT_FAILED = 0;
	
	public Map execute(); 

}
