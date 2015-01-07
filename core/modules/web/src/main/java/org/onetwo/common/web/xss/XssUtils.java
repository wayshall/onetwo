package org.onetwo.common.web.xss;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.view.jsp.tools.ToolEl;

public final class XssUtils {


	public static Object escapeIfNeccessary(Object value){
		return escapeIfNeccessary(value, null);
	}
	
	public static Object escapeIfNeccessary(Object value, Object def){
		if(value==null){
			return def;
		}else if(CharSequence.class.isInstance(value)){
			return ToolEl.escape(value.toString());
		}
		return value;
	}
	
	public static String[] escapeArray(String[] values){
    	if(LangUtils.isEmpty(values)){
    		return values;
    	}
    	for (int i = 0; i < values.length; i++) {
			values[i] = escapeIfNeccessary(values[i], "").toString();
		}
    	return values;
    }
	
	private XssUtils(){
	}
}
