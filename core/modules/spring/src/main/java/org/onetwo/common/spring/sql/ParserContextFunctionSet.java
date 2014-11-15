package org.onetwo.common.spring.sql;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class ParserContextFunctionSet {
	public static final String[] SQL_KEY_WORKDS = new String[]{" ", ";", ",", "(", ")", "'", "\"\"", "/", "+", "-"};
	
	public static final String CONTEXT_KEY = "_func";//helper
	private static final ParserContextFunctionSet instance = new ParserContextFunctionSet();
	
	public static ParserContextFunctionSet getInstance() {
		return instance;
	}

	public boolean isEmpty(Object obj){
		return LangUtils.size(obj)==0;
	}
	public boolean isBlank(Object obj){
		if(String.class.isInstance(obj))
			return StringUtils.isBlank(obj.toString());
		return LangUtils.size(obj)==0;
	}
	
	public String check(String sqlValue){
		Assert.notNull(sqlValue);
		for(String str : SQL_KEY_WORKDS){
			if(sqlValue.indexOf(str)!=-1)
				LangUtils.throwBaseException("sql value is unsafe : " + sqlValue);
		}
		return sqlValue;
	}
	
	@Deprecated
	public String inParams(String name, int size){
		StringBuilder str = new StringBuilder();
//		str.append("(");
		for(int i=0; i<size; i++){
			if(i!=0)
				str.append(", ");
			str.append(":").append(name).append(i);
		}
//		str.append(")");
		return str.toString();
	}

	public String paramIn(String name, Object inValue){
		int size = 0;
		if(Collection.class.isInstance(inValue)){
			size = ((Collection<?>)inValue).size(); 
		}else if(inValue.getClass().isArray()){
			size = Array.getLength(inValue);
		}else{
			throw new IllegalArgumentException("only supported array or collection: " + inValue.getClass());
		}
		StringBuilder str = new StringBuilder();
		str.append("(");
		for(int i=0; i<size; i++){
			if(i!=0)
				str.append(", ");
			str.append(":").append(name).append(i);
		}
		str.append(")");
		return str.toString();
	}

	@Deprecated
	public String inValue(String name, Object inValue){
		return paramIn(name, inValue);
	}
	
	public String join(String[] strs, String joiner){
		return StringUtils.join(strs, joiner);
	}

	public String nowAs(String pattern){
		return dateAs(new Date(), pattern);
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
