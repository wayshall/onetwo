package org.onetwo.common.date.timegen;

import java.util.List;

import org.onetwo.common.date.DateRange;
import org.onetwo.common.date.timegen.TimeRule.RuleType;
import org.onetwo.common.utils.Assert;

import com.google.common.collect.ImmutableList;

public class ExactDateGenerator implements DateGenerator {
	
	public RuleType getRuleType(){
		return RuleType.EXACT_DATE;
	}

	public List<DateRange> generate(TimeRule rule){
		Assert.state(rule.getRuleType()==RuleType.EXACT_DATE);
		Assert.notNull(rule.getStartTime());
		Assert.notNull(rule.getEndTime());
		return ImmutableList.of(new DateRange(rule.getStartTime(), rule.getEndTime()));
	}
}
