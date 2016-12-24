package org.onetwo.common.db.filequery;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.db.QueryContextVariable;
import org.onetwo.common.db.SqlUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class ParserContextFunctionSet implements QueryContextVariable {
	
	public static final String CONTEXT_KEY = "_func";//helper
	private static final ParserContextFunctionSet instance = new ParserContextFunctionSet();
	
	public static ParserContextFunctionSet getInstance() {
		return instance;
	}
	
	

	@Override
	public String varName() {
		return CONTEXT_KEY;
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
		return SqlUtils.check(sqlValue);
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

	/****
	 * spring named sql supported List value
	 * ${_func.paramIn('dptcode', dptcodeList)}
	 * @param name
	 * @param inValue
	 * @return
	 */
	public String paramIn(String name, Object inValue){
		int size = 0;
		if(Collection.class.isInstance(inValue)){
			size = ((Collection<?>)inValue).size(); 
		}else if(inValue!=null && inValue.getClass().isArray()){
			size = Array.getLength(inValue);
		}else{
			throw new IllegalArgumentException("only supported array or collection: " + inValue);
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
			val = DateUtils.format(pattern, (Date)date);
		}else if(String.class.isInstance(date)){
			val = DateUtils.format(pattern, DateUtils.parse(date.toString()));
		}else{
			throw new IllegalArgumentException("type: " + date.getClass());
		}
		return val;
	}

	private ParserContextFunctionSet(){
	}

}
