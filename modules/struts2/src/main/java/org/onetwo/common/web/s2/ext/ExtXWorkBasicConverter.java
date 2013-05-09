package org.onetwo.common.web.s2.ext;

import java.lang.reflect.Member;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.impl.XWorkBasicConverter;

/***
 * 处理linux下struts2日期问题
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class ExtXWorkBasicConverter extends XWorkBasicConverter {
	
	public static final String BEAN_NAME = "extXWorkBasicConverter";
	
	public Object convertValue(Map<String, Object> context, Object o, Member member, String s, Object value, Class toType) {
		if(context==null)
			context = OgnlUtils.getDefaultOgnlContext();
		if(Date.class.isAssignableFrom(toType) || Calendar.class.isAssignableFrom(toType))
			context.put(ActionContext.LOCALE, Locale.CHINA);
		Object result = null;
		result = super.convertValue(context, o, member, s, value, toType);
		return result;
	}
	
	public static void  main(String[] args){
		System.out.println("aaa_bb".split("_")[0]);
	}
}
