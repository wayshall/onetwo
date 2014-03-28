package org.onetwo.common.excel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.onetwo.common.excel.data.ExcelValueParser;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.TheFunction;

import com.google.common.collect.Maps;

public class DefaultExcelValueParser implements ExcelValueParser {
	public static final Pattern IS_DIGIT = Pattern.compile("^\\d+$");

	private Map<String, Object> context;
	private Map<String, Object> varMap = Maps.newHashMap();
	
	public DefaultExcelValueParser(){
		this(null);
	}
	
	public DefaultExcelValueParser(Map<String, Object> context) {
		super();
		if(context==null)
			context = new HashMap<String, Object>();
		else
			this.context = context;
		if(!context.containsKey(TheFunction.ALIAS_NAME)){
			context.put(TheFunction.ALIAS_NAME, TheFunction.getInstance());
		}
	}
	
	public void putVar(String name, Object value){
		this.varMap.put(name, value);
		this.context.put(name, value);
	}

	public Map<String, Object> getContext() {
		return context;
	} 

	/*public Object parseValue(String expr, Object obj, Object objForSymbol){
		if(StringUtils.isBlank(expr))
			return "";
		Object fieldValue = null;
		if(isSymbol(expr)){
			fieldValue = parseSymbol(expr, obj);
		}else{
			if(expr.startsWith("#")){
				fieldValue = varMap.get(expr.substring(1));
				if(fieldValue!=null)
					return fieldValue;
			}
			fieldValue = ExcelUtils.getValue(expr, context, obj);
		}
		
		return fieldValue;
	}*/

	public Object parseValue(String expr, Object root, Map<String, Object> context){
		if(StringUtils.isBlank(expr))
			return "";
		Object fieldValue = null;
		if(isSymbol(expr)){
			fieldValue = parseSymbol(expr, root);
		}else{
			if(expr.startsWith("#")){
				fieldValue = varMap.get(expr.substring(1));
				if(fieldValue!=null)
					return fieldValue;
			}
			fieldValue = ExcelUtils.getValue(expr, context, root);
		}
		return fieldValue;
	}

	public Object parseValue(String expr){
		return parseValue(expr, null, context);
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

//	@Override
	public int parseIntValue(String expr) {
		return parseIntValue(expr, null);
	}

	public int parseIntValue(String expr, Object root) {
		if(StringUtils.isBlank(expr)){
			return 0;
		}
		
		if(IS_DIGIT.matcher(expr).matches()){
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
