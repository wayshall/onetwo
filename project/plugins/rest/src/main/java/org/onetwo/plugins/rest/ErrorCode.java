package org.onetwo.plugins.rest;

final public class ErrorCode {

	private ErrorCode(){
	}


	public static final Integer RET_SUCCEED = 1;
	public static final Integer RET_FAILED = 0;
	
	/****
	 * 未知或未定义的错误
	 */
	public static final String UN_KNOWN = "un_known";
	/****
	 * Json转对象出错
	 */
	public static final String COM_ER_JSON_TO_OBJECT = "com_er_json_to_object";
	/*******
	 * 对象转json出错
	 */
	public static final String COM_ER_OBJECT_TO_JSON = "com_er_object_to_json";
	/*******
	 * 验证错误
	 */
	public static final String COM_ER_VALIDATION = "com_er_validation";
	
	/********
	 * 必填字段为null
	 */
	public static final String COM_ER_VALIDATION_REQUIRED = "com_er_validation_required";
	/*******
	 * Id查询数据不存在
	 */
	public static final String COM_ER_ID_NONEXISTENT = "com_er_id_nonexistent";
	
	/*******
	 * 系统错误
	 */
	public static final String SYSTEM_ERROR = "system_error";
	
}
