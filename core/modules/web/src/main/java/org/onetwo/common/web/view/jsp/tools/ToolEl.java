package org.onetwo.common.web.view.jsp.tools;

import java.util.Date;

import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.StringUtils;
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

	private ToolEl(){}
}
