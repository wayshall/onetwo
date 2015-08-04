package org.onetwo.common.web.xss;

import org.onetwo.common.utils.LangUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

public final class XssUtils {


	public static String escape(String content){
		return HtmlUtils.htmlEscape(JavaScriptUtils.javaScriptEscape(content));
	}

	public static String unescape(String content){
		return HtmlUtils.htmlUnescape(content);
	}

	public static String escapeIfNeccessary(String value){
		return escapeIfNeccessary(value, null);
	}
	
	public static String escapeIfNeccessary(String value, String def){
		if(value==null){
			return def;
		}else{
			return escape(value);
//			return ToolEl.escapeHtml(ToolEl.escapeJs(value.toString()));
		}
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
