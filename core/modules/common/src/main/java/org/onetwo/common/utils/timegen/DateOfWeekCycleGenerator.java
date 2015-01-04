package org.onetwo.common.utils.timegen;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DateRange;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.timegen.TimeRule.RuleType;

public class DateOfWeekCycleGenerator implements DateGenerator {
	
	public RuleType getRuleType(){
		return RuleType.PERIOD_WEEK;
	}

	public Collection<DateRange> generate(TimeRule rule){
		Assert.state(rule.getRuleType()==RuleType.PERIOD_WEEK);
		
		Collection<DateRange> drs = DateUtil.splitAsDateRangeByWeek(rule.getStartTime(), rule.getEndTime());
		Collection<DateRange> genDateRangs = new LinkedHashSet<DateRange>(drs.size());
		for(DateRange dr : drs){
			int startIndex = rule.getStartIndex();
			if(startIndex<dr.getStart().getDayOfWeek())
				startIndex = dr.getStart().getDayOfWeek();
			int endIndex = rule.getEndIndex();
			if(endIndex>dr.getEnd().getDayOfWeek())
				endIndex = dr.getEnd().getDayOfWeek();
			if(startIndex>endIndex)
				continue;
			DateRange newDr = new DateRange(dr.getStart().withDayOfWeek(startIndex), dr.getStart().withDayOfWeek(endIndex));
			genDateRangs.add(newDr);
		}

		return genDateRangs;
	}
}
