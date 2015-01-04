package org.onetwo.common.utils;

import java.util.Date;

import org.joda.time.LocalDate;

public class DateRange {

	final private LocalDate start;
	final private LocalDate end;
	

	public DateRange(LocalDate startDate, LocalDate endDate) {
		Assert.notNull(startDate);
		Assert.notNull(endDate);
		this.start = startDate;
		this.end = endDate;
		this.checkInterval();
	}
	
	protected void checkInterval(){
		if(start.isAfter(end))
			throw new IllegalArgumentException("the start can not greater than end");
	}

	public DateRange(Date startDate, Date endDate) {
		Assert.notNull(startDate);
		Assert.notNull(endDate);
		this.start = new LocalDate(startDate);
		this.end = new LocalDate(endDate);
		this.checkInterval();
	}

	public NiceDate getStartNiceDate() {
		return NiceDate.New(start.toDate());
	}

	public NiceDate getEndNiceDate() {
		return NiceDate.New(end.toDate());
	}

	public LocalDate getStart() {
		return start;
	}

	public LocalDate getEnd() {
		return end;
	}
	
}
