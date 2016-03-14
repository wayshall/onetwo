package org.onetwo.common.date;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

final public class DateRangeStaticFacotry {
	
	private DateRangeStaticFacotry(){
	}

	public static Collection<DateRange> splitAsDateRangeByWeek(Date startDate, Date endDate){
		LocalDate start = new LocalDate(startDate);
		LocalDate end = new LocalDate(endDate);
		return splitAsDateRangeByWeek(start, end);
	}
	public static Collection<DateRange> splitAsDateRangeByWeek(LocalDate start, LocalDate end){
		
		Set<DateRange> dates = new LinkedHashSet<DateRange>();
		dates.add(new DateRange(start, start.withDayOfWeek(DateTimeConstants.SUNDAY)));
		
		LocalDate startDateOfWeek = start.withDayOfWeek(DateTimeConstants.MONDAY).plusWeeks(1);
		while(!startDateOfWeek.isAfter(end)){
			LocalDate endDateOfWeek = startDateOfWeek.withDayOfWeek(DateTimeConstants.SUNDAY);
			if(endDateOfWeek.isAfter(end)){
				endDateOfWeek = end;
			}
			dates.add(new DateRange(startDateOfWeek, endDateOfWeek));
			startDateOfWeek = startDateOfWeek.plusWeeks(1);
		}
		return dates;
	}


	public static Collection<DateRange> splitAsDateRangeByMonth(Date startDate, Date endDate){
		LocalDate start = new LocalDate(startDate);
		LocalDate end = new LocalDate(endDate);
		return splitAsDateRangeByMonth(start, end);
	}
	public static Collection<DateRange> splitAsDateRangeByMonth(LocalDate start, LocalDate end){
		
		Set<DateRange> dates = new LinkedHashSet<DateRange>();
		dates.add(new DateRange(start, start.withDayOfMonth(start.dayOfMonth().getMaximumValue())));
		
		LocalDate startDateOfMonth = start.withDayOfMonth(start.dayOfMonth().getMinimumValue()).plusMonths(1);
		while(!startDateOfMonth.isAfter(end)){
			LocalDate endDateOfWeek = startDateOfMonth.withDayOfMonth(startDateOfMonth.dayOfMonth().getMaximumValue());
			if(endDateOfWeek.isAfter(end)){
				endDateOfWeek = end;
			}
			DateRange dr = new DateRange(startDateOfMonth, endDateOfWeek);
			dates.add(dr);
			startDateOfMonth = startDateOfMonth.plusMonths(1);
		}
		return dates;
	}
}
