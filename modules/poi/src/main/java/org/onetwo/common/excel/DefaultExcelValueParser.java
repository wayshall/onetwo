package org.onetwo.common.excel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.TheFunction;

@SuppressWarnings("rawtypes")
public class DefaultExcelValueParser implements ExcelValueParser {
	
	private Map context;
	
	public DefaultExcelValueParser(){
		this(null);
	}
	
	@SuppressWarnings("unchecked")
	public DefaultExcelValueParser(Map context) {
		super();
		if(context==null)
			context = new HashMap();
		else
			this.context = context;
		if(!context.containsKey(TheFunction.ALIAS_NAME)){
			context.put(TheFunction.ALIAS_NAME, TheFunction.getInstance());
		}
	}

	public Map getContext() {
		return context;
	} 

	public Object parseValue(String expr, Object obj, Object objForSymbol){
		if(StringUtils.isBlank(expr))
			return "";
		Object fieldValue = null;
		if(isSymbol(expr))
			fieldValue = parseSymbol(expr, obj);
		else
			fieldValue = ExcelUtils.getValue(expr, context, obj);
		
		return fieldValue;
	}
	
	public boolean isSymbol(String value){
		if(value==null || !value.startsWith(":"))
			return false;
		return true;
	}
	
	public boolean isRef(String value){
		if(value==null || !value.startsWith("&"))
			return false;
		return true;
	}
	
	public String getSymbol(String value){
		if(!isSymbol(value))
			return null;
		return value.substring(1);
	}
	
	public Object parseSymbol(String value, Object obj){
		if(obj==null)
			return null;
		String symbol = getSymbol(value);
		try {
			Object v = ReflectUtils.getProperty(obj, symbol, false);
			return v;
		} catch (Exception e) {
			throw new ServiceException("parseSymbol error on["+obj+"] : "+ value);
		}
	}

	public int parseIntValue(String expr, Object root) {
		if(StringUtils.isBlank(expr)){
			return 0;
		}
		
		if(FieldModel.IS_DIGIT.matcher(expr).matches()){
			return Integer.parseInt(expr);
		}
		
		Number v = (Number)parseValue(expr, root, null);
		return v==null?0:v.intValue();
	}
	
	public int parseIntValue2(String expr, Object root) {
		Object v = parseValue(expr, root, null);
		return (v == null ? 0 : Integer.valueOf(v.toString()));		
	}
	
}
