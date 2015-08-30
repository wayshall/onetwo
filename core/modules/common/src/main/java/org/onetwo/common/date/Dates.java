package org.onetwo.common.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/***
 * date utils for java8
 * @author way
 *
 */
final public class Dates {
	
	
	public static Date toDate(LocalDate localDate){
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static Date toDate(LocalDateTime localDateTime){
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDate toLocalDate(Date date) {
	    return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	  }

	public static LocalDateTime toLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
  
//	public static long sum(Iterable<T>)
	
	private Dates(){}

}
