package org.onetwo.common.web.view.jsp.tools;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.preventor.PreventorFactory;
import org.onetwo.common.web.preventor.RequestPreventor;
import org.onetwo.common.web.utils.WebHolder;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.common.web.xss.XssUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.web.util.HtmlUtils;

final public class ToolEl {
	private static RequestPreventor CSRF_PREVENTOR = PreventorFactory.getCsrfPreventor();
	private static boolean escapeWebConent = !BaseSiteConfig.getInstance().isPreventXssRequest();

	/****
	 * use spring utils
	 * @param content
	 * @return
	 */
	public static String escape(String content){
		return XssUtils.escape(content);
	}
	/***
	 * use spring utils
	 * @param content
	 * @return
	 */
	public static String unescape(String content){
		return XssUtils.unescape(content);
	}
	
	public static String escapeHtml(String content){
//		return HtmlUtils.htmlEscape(content);
		return StringEscapeUtils.escapeHtml4(content);
	}

	public static String escapeJs(String content){
//		return JavaScriptUtils.javaScriptEscape(content);
		return StringEscapeUtils.escapeEcmaScript(content);
	}

	public static String unescapeHtml(String content){
		return StringEscapeUtils.unescapeHtml4(content);
	}

	public static String unescapeJs(String content){
		return StringEscapeUtils.unescapeEcmaScript(content);
	}
	
	public static String firstNotblank(String val, String def1, String def2){
		return escapeHtml(StringUtils.firstNotBlank(new String[]{val, def1, def2}));
	}
	
	public static String format(Date date, String pattern){
		return DateUtil.formatDateByPattern(date, pattern);
	}

	
	public String year(Date date){
		return DateUtil.format("yyyy", date);
	}
	
	public String month(Date date){
		return DateUtil.format("MM", date);
	}
	
	public static String asDateTime(Date date){
		return DateUtil.formatDateTime(date);
	}
	
	public static String asDate(Date date){
		return DateUtil.formatDate(date);
	}
	
	public static String asTime(Date date){
		return DateUtil.formatTime(date);
	}
	
	public static String attr(String attr, String val){
		if(StringUtils.isBlank(val))
			return "";
		StringBuilder attributesBuf = new StringBuilder();
		attributesBuf.append(attr).append("=\"").append(HtmlUtils.htmlEscape(val)).append("\"");
		return attributesBuf.toString();
	}
	
	public static String attrs(String attr, String val1, String val2){
		if(StringUtils.isBlank(val1) && StringUtils.isBlank(val2))
			return "";
		StringBuilder attributesBuf = new StringBuilder();
		attributesBuf.append(attr).append("=\"");
		if(StringUtils.isBlank(val1)){
			attributesBuf.append(HtmlUtils.htmlEscape(val1)).append(" ");
		}
		if(StringUtils.isBlank(val2)){
			attributesBuf.append(HtmlUtils.htmlEscape(val2));
		}
		attributesBuf.append("\"");
		return attributesBuf.toString();
	}
	
	public static Object exec(Object target, String methodName, Object arg){
		if(StringUtils.isBlank(methodName))
			return null;
		return ReflectUtils.invokeMethod(methodName, target, arg);
	}
	
	public static boolean checked(Object target, String methodName, Object arg){
		if(target==null || StringUtils.isBlank(methodName))
			return false;
		BeanWrapper bw = SpringUtils.newBeanWrapper(target);
		if(bw.isReadableProperty(methodName)){
			Object selectValue = bw.getPropertyValue(methodName);
			BeanWrapper argBw = SpringUtils.newBeanWrapper(arg);
			if(!argBw.isReadableProperty(methodName)){
				throw new BaseException("not property["+methodName+"] found in object: " + arg);
			}
			return LangUtils.equals(selectValue, SpringUtils.newBeanWrapper(arg).getPropertyValue(methodName));
		}
		return (Boolean)ReflectUtils.checkAndInvokeMethod(methodName, target, arg);
	}
	
	public static String checkedHtml(Object target, String methodName, Object arg){
		return checked(target, methodName, arg)?"checked=true":"";
	}
	
	public static String addParam(String action, String name, String value){
		return TagUtils.appendParam(action, name, value);
	}
	
	public static String join(String[] strs, String joiner){
		return StringUtils.join(strs, joiner);
	}
	
	public static String joinC(Collection<String> strs, String joiner){
		return StringUtils.join(strs, joiner);
	}
	
	public static String web(String name){
		String value = WebHolder.getValue(name).toString();
		if(escapeWebConent){
			value = escape(value);
		}
		return value;
	}
	
	public static String json(Object value){
		return JsonMapper.defaultMapper().toJson(value);
	}
	
	public static String safeUrl(String href){
		String url = BaseSiteConfig.getInstance().getBaseURL();
    	if(BaseSiteConfig.getInstance().isSafeRequest()){
    		url += CSRF_PREVENTOR.processSafeUrl(href, WebHolder.getRequest(), WebHolder.getResponse());
    	}else{
    		url += href;
    	}
    	return url;
	}

	private ToolEl(){}
}
