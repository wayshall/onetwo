package org.onetwo.common.utils;


import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.onetwo.common.date.DateUtil;

public class JodatimeUtils {
	
	public static String[] DATE_PATTERNS = new String[]{"yyyy-MM-dd"};
	public static String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	
	public static DateTime atStartOfDate(Date date){
		DateTime dt = new DateTime(date).withTimeAtStartOfDay();
		return dt;
	}
	
	public static DateTime atEndOfDate(Date date){
		DateTime dt = new DateTime(date).withTimeAtStartOfDay().plusDays(1).minusMillis(1);
		return dt;
	}
	
	public static DateTime atStartOfMonth(Date date){
		DateTime dt = new DateTime(date).dayOfMonth().withMinimumValue();
//		return dt.toDate();
		return dt;
	}
	
	public static DateTime atEndOfMonth(Date date){
		DateTime dt = new DateTime(date).dayOfMonth().withMaximumValue();
		return dt;
	}
	
	public static String format(Date date, String pattern){
		return new DateTime(date).toString(pattern);
	}
	public static String formatDateTime(Date date){
		return new DateTime(date).toString(PATTERN_DATE_TIME);
	}
	
	public static DateTime parse(String source, String pattern){
		return DateTimeFormat.forPattern(pattern).parseDateTime(source);
	}
	
	public static DateTime parse(String source){
		return DateTimeFormat.forPattern(DateUtil.matchPattern(source)).parseDateTime(source);
	}

	private JodatimeUtils(){
	}
}
