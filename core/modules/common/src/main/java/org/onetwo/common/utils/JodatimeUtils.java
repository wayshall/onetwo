package org.onetwo.common.utils;


import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class JodatimeUtils {
	
	public static Date atStartOfDate(Date date){
		DateTime dt = new DateTime(date).withTimeAtStartOfDay();
		return dt.toDate();
	}
	
	public static Date atEndOfDate(Date date){
		DateTime dt = new DateTime(date).withTimeAtStartOfDay().plusDays(1).minusMillis(1);
		return dt.toDate();
	}
	
	public static Date atStartOfMonth(Date date){
		DateTime dt = new DateTime(date).dayOfMonth().withMinimumValue();
		return dt.toDate();
	}
	
	public static Date atEndOfMonth(Date date){
		DateTime dt = new DateTime(date).dayOfMonth().withMaximumValue();
		return dt.toDate();
	}
	
	public static String format(Date date, String pattern){
		return new DateTime(date).toString(pattern);
	}
	
	public static Date parse(String source, String pattern){
		return DateTimeFormat.forPattern(pattern).parseDateTime(source).toDate();
	}

	private JodatimeUtils(){
	}
}
