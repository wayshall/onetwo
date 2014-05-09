package org.onetwo.common.spring.sql;

import java.util.Date;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.StringUtils;

public class ParserContextFunctionSet {
	public static final String CONTEXT_KEY = "_func";//helper
	private static final ParserContextFunctionSet instance = new ParserContextFunctionSet();
	
	public static ParserContextFunctionSet getInstance() {
		return instance;
	}
	
	public String join(String[] strs, String joiner){
		return StringUtils.join(strs, joiner);
	}

	public String dateAs(Object date, String pattern){
		Assert.notNull(date);
		String val = null;
		if(Date.class.isInstance(date)){
			val = DateUtil.format(pattern, (Date)date);
		}else if(String.class.isInstance(date)){
			val = DateUtil.format(pattern, DateUtil.parse(date.toString()));
		}else{
			throw new IllegalArgumentException("type: " + date.getClass());
		}
		return val;
	}

	private ParserContextFunctionSet(){
	}

}
