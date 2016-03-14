package org.onetwo.common.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/***
 * date utils for java8
 * @author way
 *
 */
final public class Dates {

	public static final DateTimeFormatter YEAR_ONLY = DateTimeFormatter.ofPattern(DateUtil.YEAR_ONLY);
	public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern(DateUtil.YEAR_MONTH);
	public static final DateTimeFormatter DATE_ONLY = DateTimeFormatter.ofPattern(DateUtil.DATE_ONLY);
	public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern(DateUtil.DATE_TIME);
	public static final DateTimeFormatter DATE_TIME_MILLS = DateTimeFormatter.ofPattern(DateUtil.DATE_TIME_MILLS);
	public static final DateTimeFormatter DATE_TIME_MILLS2 = DateTimeFormatter.ofPattern(DateUtil.DATE_TIME_MILLS2);
	public static final DateTimeFormatter DATE_SHORT_TIME = DateTimeFormatter.ofPattern(DateUtil.DATE_SHORT_TIME);
	public static final DateTimeFormatter TIME_ONLY = DateTimeFormatter.ofPattern(DateUtil.TIME_ONLY);
	public static final DateTimeFormatter SHORT_TIME_ONLY = DateTimeFormatter.ofPattern(DateUtil.SHORT_TIME_ONLY);
	
	public static final DateTimeFormatter DATEONLY = DateTimeFormatter.ofPattern(DateUtil.DATEONLY);
	public static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern(DateUtil.DATETIME);
	public static final DateTimeFormatter TIMEONLY = DateTimeFormatter.ofPattern(DateUtil.TIMEONLY);

	public static String formatDateTime(ChronoLocalDate localDate){
		return localDate.format(DATE_TIME);
	}
	public static String formatDate(ChronoLocalDate localDate){
		return localDate.format(DATE_ONLY);
	}
	
	public static Date toDate(LocalDate localDate){
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static Date toDate(LocalDateTime localDateTime){
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDate toLocalDate(Date date) {
	    return toZonedDateTime(date).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return toZonedDateTime(date).toLocalDateTime();
	}

	public static LocalTime toLocalTime(Date date) {
		return toZonedDateTime(date).toLocalTime();
	}

	public static ZonedDateTime toZonedDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault());
	}
  
//	public static long sum(Iterable<T>)
	
	private Dates(){}

}
