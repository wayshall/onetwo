package org.onetwo.common.web.view.jsp.tools;

import java.util.Date;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.web.util.HtmlUtils;

final public class ToolEl {

	public static String escapeHtml(String content){
		return HtmlUtils.htmlEscape(content);
	}
	
	public static String firstNotblank(String val, String def1, String def2){
		return escapeHtml(StringUtils.firstNotBlank(new String[]{val, def1, def2}));
	}
	
	public static String format(Date date, String pattern){
		return DateUtil.formatDateByPattern(date, pattern);
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
		if(StringUtils.isBlank(methodName))
			return false;
		BeanWrapper bw = SpringUtils.newBeanWrapper(target);
		if(bw.isReadableProperty(methodName)){
			Object selectValue = bw.getPropertyValue(methodName);
			return LangUtils.equals(selectValue, SpringUtils.newBeanWrapper(arg).getPropertyValue(methodName));
		}
		return (Boolean)ReflectUtils.invokeMethod(methodName, target, arg);
	}
	
	public static String checkedHtml(Object target, String methodName, Object arg){
		return checked(target, methodName, arg)?"checked=true":"";
	}
	
	public static String addParam(String action, String name, String value){
		return TagUtils.appendParam(action, name, value);
	}
	
	public static String web(String name){
		return escapeHtml(WebHolder.getValue(name).toString());
	}

	private ToolEl(){}
}
