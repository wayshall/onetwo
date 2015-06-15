package org.onetwo.common.db;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

final public class SqlUtils {
	public static final String[] SQL_KEY_WORKDS = new String[]{" ", ";", ",", "(", ")", "'", "\"\"", "/", "+", "-"};

	public static String check(String sqlValue){
		if(StringUtils.isBlank(sqlValue)){
			return sqlValue;
		}
		for(String str : SQL_KEY_WORKDS){
			if(sqlValue.indexOf(str)!=-1)
				LangUtils.throwBaseException("sql value is unsafe : " + sqlValue);
		}
		return sqlValue;
	}
	
	
	private SqlUtils(){
	}
}
