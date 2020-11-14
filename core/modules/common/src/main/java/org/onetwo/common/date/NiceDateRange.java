package org.onetwo.common.date;

import java.util.Date;

import org.onetwo.common.utils.Assert;

public class NiceDateRange {

	final private NiceDate start;
	final private NiceDate end;

	public NiceDateRange(Date startDate, Date endDate) {
		this(NiceDate.New(startDate), NiceDate.New(endDate));
	}
	
	public NiceDateRange(NiceDate startDate, NiceDate endDate) {
		Assert.notNull(startDate);
		Assert.notNull(endDate);
		this.start = startDate;
		this.end = endDate;
		this.checkInterval();
	}
	
	final protected void checkInterval(){
		if(start.isAfter(end))
			throw new IllegalArgumentException("the start["+start+"] can not greater than end["+end+"]");
	}
	
	public static final class QuarterDateRange extends NiceDateRange {
		final private int quarterValue;

		public QuarterDateRange(int quarter, NiceDate startDate, NiceDate endDate) {
			super(startDate, endDate);
			this.quarterValue = quarter;
		}

		public int getValue() {
			return quarterValue;
		}
		
		public QuarterDateRange nextQuarter(int diff) {
			int nextQuarter = Math.abs(this.quarterValue + diff)%4;
			return NiceDate.New().getQuarter(nextQuarter);
		}
		
		public String toString() {
			return toString("", "Q");
		}
		
		public String toString(String joiner, String quarterTag) {
			return this.getStart().format("yyyy") + joiner + quarterTag + (this.quarterValue + 1);
		}
		
	}

	public NiceDate getStart() {
		return start;
	}

	public NiceDate getEnd() {
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
		NiceDateRange other = (NiceDateRange) obj;
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
		return "NiceDateRange [start=" + start + ", end=" + end + "]";
	}
	
}
