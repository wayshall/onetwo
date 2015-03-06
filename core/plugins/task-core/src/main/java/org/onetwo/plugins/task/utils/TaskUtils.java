package org.onetwo.plugins.task.utils;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.LangUtils;

public class TaskUtils {

	public static final String EMAIL_SPLITTER = ";";
	public static final String ATTACHMENT_PATH_SPLITTER = ";";
	public static final char ATTACHMENT_PATH_SPLITTER_CHAR = ';';
	public static final String SEQ_TABLE_NAME = "SEQ_TABLES";
	

	public static final int MAX_LIMIT_LENGTH = 4000;
	

	public static String asJsonStringWithMaxLimit(Object result){
		if(result==null)
			return LangUtils.EMPTY_STRING;
		if(String.class.isInstance(result)){
			return maxLimitString(result.toString());
		}
		String str = JsonMapper.IGNORE_EMPTY.toJson(result);
		return maxLimitString(str);
	}
	
	public static String maxLimitString(String str){
		if(str.length()>MAX_LIMIT_LENGTH){
			str = str.substring(0, MAX_LIMIT_LENGTH);
		}
		return str;
	}
}
