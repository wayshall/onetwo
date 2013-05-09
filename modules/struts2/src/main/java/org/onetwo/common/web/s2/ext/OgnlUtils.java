package org.onetwo.common.web.s2.ext;

import java.util.Map;

import ognl.Ognl;
import ognl.OgnlContext;

import com.opensymphony.xwork2.conversion.impl.XWorkBasicConverter;
import com.opensymphony.xwork2.ognl.OgnlTypeConverterWrapper;

@SuppressWarnings("unchecked")
public abstract class OgnlUtils {

	private static final XWorkBasicConverter BASIC_CONVERTER = new ExtXWorkBasicConverter();

	private static final OgnlContext DEFAULT_OGNL_CONTEXT;
	
	static{
		DEFAULT_OGNL_CONTEXT = new OgnlContext();
		OgnlTypeConverterWrapper type = new OgnlTypeConverterWrapper(BASIC_CONVERTER);
		DEFAULT_OGNL_CONTEXT.setTypeConverter(type);
	}
	
	public static OgnlContext getOgnlContext(){
		OgnlContext context = new OgnlContext();
		OgnlTypeConverterWrapper type = new OgnlTypeConverterWrapper(BASIC_CONVERTER);
		context.setTypeConverter(type);
		return context;
	}
	
	public static OgnlContext getDefaultOgnlContext(){
		return DEFAULT_OGNL_CONTEXT;
	}
	
	public static Object convertValue(Object value, Class toType){
		Object v = Ognl.getTypeConverter( DEFAULT_OGNL_CONTEXT ).convertValue( null, null, null, null, value, toType);
		return v;
	}
	
	public static Object convertValue(Map context, Object target, Object value, Class toType){
		Object v = Ognl.getTypeConverter( context ).convertValue( context, target, null, null, value, toType);
		return v;
	}
}
