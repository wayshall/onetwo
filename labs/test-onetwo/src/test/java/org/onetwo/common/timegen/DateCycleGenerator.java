package org.onetwo.common.timegen;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.onetwo.common.timegen.TimeRule.RuleType;
import org.springframework.util.Assert;

public class DateCycleGenerator implements DateGenerator {
	
	public RuleType getRuleType(){
		return RuleType.PERIOD_WEEK;
	}

	public Collection<DateRange> generate(TimeRule rule){
		Assert.state(rule.getRuleType()==RuleType.PERIOD_WEEK);
		
		LocalDate start = new LocalDate(rule.getStartTime());
		LocalDate end = new LocalDate(rule.getEndTime());
		
		Set<LocalDate> dates = new LinkedHashSet<LocalDate>();
		dates.add(start);
		LocalDate weekDate = start.withDayOfWeek(DateTimeConstants.MONDAY).plusWeeks(1);
		while(weekDate.isBefore(end) || weekDate.isEqual(end)){
			dates.add(weekDate)
			println weekDate.getDayOfWeek()
			weekDate = weekDate.plusWeeks(1);
		}
	}
}
