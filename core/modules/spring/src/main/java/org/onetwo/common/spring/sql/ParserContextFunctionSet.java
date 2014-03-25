package org.onetwo.common.spring.sql;

import org.onetwo.common.utils.StringUtils;

public class ParserContextFunctionSet {
	public static final String CONTEXT_KEY = "helper";
	private static final ParserContextFunctionSet instance = new ParserContextFunctionSet();
	
	public static ParserContextFunctionSet getInstance() {
		return instance;
	}
	
	public String join(String[] strs, String joiner){
		return StringUtils.join(strs, joiner);
	}


	private ParserContextFunctionSet(){
	}

}
