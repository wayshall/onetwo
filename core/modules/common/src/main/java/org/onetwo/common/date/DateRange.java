package org.onetwo.common.date;

import java.util.Date;

import org.joda.time.LocalDate;
import org.onetwo.common.utils.Assert;

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
			throw new IllegalArgumentException("the start["+start+"] can not greater than end["+end+"]");
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

	public Date getStartDate() {
		return start.toDate();
	}

	public Date getEndDate() {
		return end.toDate();
	}

	public LocalDate getStart() {
		return start;
	}

	public LocalDate getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateRange other = (DateRange) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DateRange [start=" + start + ", end=" + end + "]";
	}
	
}
