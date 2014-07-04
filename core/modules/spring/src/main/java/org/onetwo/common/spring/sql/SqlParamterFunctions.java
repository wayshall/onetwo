package org.onetwo.common.spring.sql;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.utils.StringUtils;

public class SqlParamterFunctions {
	
	final private static SqlParamterFunctions instance = new SqlParamterFunctions();
	
	
	public static SqlParamterFunctions getInstance() {
		return instance;
	}
	public String like(String value){
		return ExtQueryUtils.getLikeString(value);
	}
	public String prelike(String value){
		return StringUtils.appendStartWith(value, "%");
	}
	public String postlike(String value){
		return StringUtils.appendEndWith(value, "%");
	}
	private SqlParamterFunctions(){}

}
