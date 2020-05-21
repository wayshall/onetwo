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

	public static final DateTimeFormatter YEAR_ONLY = DateTimeFormatter.ofPattern(DateUtils.YEAR_ONLY);
	public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern(DateUtils.YEAR_MONTH);
	public static final DateTimeFormatter DATE_ONLY = DateTimeFormatter.ofPattern(DateUtils.DATE_ONLY);
	public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern(DateUtils.DATE_TIME);
	public static final DateTimeFormatter DATE_TIME_MILLS = DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_MILLS);
	public static final DateTimeFormatter DATE_TIME_MILLS2 = DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_MILLS2);
	public static final DateTimeFormatter DATE_SHORT_TIME = DateTimeFormatter.ofPattern(DateUtils.DATE_SHORT_TIME);
	public static final DateTimeFormatter TIME_ONLY = DateTimeFormatter.ofPattern(DateUtils.TIME_ONLY);
	public static final DateTimeFormatter SHORT_TIME_ONLY = DateTimeFormatter.ofPattern(DateUtils.SHORT_TIME_ONLY);
	
	public static final DateTimeFormatter DATEONLY = DateTimeFormatter.ofPattern(DateUtils.DATEONLY);
	public static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern(DateUtils.DATETIME);
	public static final DateTimeFormatter TIMEONLY = DateTimeFormatter.ofPattern(DateUtils.TIMEONLY);

	public static String formatDateTime(ChronoLocalDate localDate){
		return localDate.format(DATE_TIME);
	}
	public static String formatDate(ChronoLocalDate localDate){
		return localDate.format(DATE_ONLY);
	}
	
	public static LocalDateTime parseDateTime(String dateTimeStr){
		return LocalDateTime.parse(dateTimeStr, DATE_TIME);
	}
	
	public static Date toDate(LocalDate localDate, LocalTime localTime){
		LocalDateTime newDate = localTime.atDate(localDate);
		return Date.from(newDate.atZone(ZoneId.systemDefault()).toInstant());
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
	
	public static LocalDate toLocalDate(Long millis) {
	    return toZonedDateTime(millis).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return toZonedDateTime(date).toLocalDateTime();
	}

	public static LocalDateTime toLocalDateTime(Long millis) {
		return toZonedDateTime(millis).toLocalDateTime();
	}

	public static LocalTime toLocalTime(Date date) {
		return toZonedDateTime(date).toLocalTime();
	}

	public static LocalTime toLocalTime(Long millis) {
		return toZonedDateTime(millis).toLocalTime();
	}

	public static ZonedDateTime toZonedDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault());
	}

	public static ZonedDateTime toZonedDateTime(Long millis) {
		return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault());
	}
  
//	public static long sum(Iterable<T>)
	
	private Dates(){}

}
