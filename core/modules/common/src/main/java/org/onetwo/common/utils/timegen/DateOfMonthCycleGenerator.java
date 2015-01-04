package org.onetwo.common.utils.timegen;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DateRange;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.timegen.TimeRule.RuleType;

public class DateOfMonthCycleGenerator implements DateGenerator {
	
	public RuleType getRuleType(){
		return RuleType.PERIOD_MONTH;
	}

	public Collection<DateRange> generate(TimeRule rule){
		Assert.state(rule.getRuleType()==RuleType.PERIOD_MONTH);
		
		Collection<DateRange> drs = DateUtil.splitAsDateRangeByMonth(rule.getStartTime(), rule.getEndTime());
		Collection<DateRange> genDateRangs = new LinkedHashSet<DateRange>(drs.size());
		for(DateRange dr : drs){
			int startIndex = rule.getStartIndex();
			if(startIndex<dr.getStart().getDayOfMonth())
				startIndex = dr.getStart().getDayOfMonth();
			int endIndex = rule.getEndIndex();
			if(endIndex>dr.getEnd().getDayOfMonth())
				endIndex = dr.getEnd().getDayOfMonth();
			if(startIndex>endIndex)
				continue;
			DateRange newDr = new DateRange(dr.getStart().withDayOfMonth(startIndex), dr.getStart().withDayOfMonth(endIndex));
			System.out.println("new dir: " + newDr);
			genDateRangs.add(newDr);
		}

		return genDateRangs;
	}
}
