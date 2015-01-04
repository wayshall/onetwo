package org.onetwo.common.timegen;

import java.util.List;

import org.onetwo.common.timegen.TimeRule.RuleType;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;

public class ExactDateGenerator implements DateGenerator {
	
	public RuleType getRuleType(){
		return RuleType.EXACT_DATE;
	}

	public List<DateRange> generate(TimeRule rule){
		Assert.state(rule.getRuleType()==RuleType.EXACT_DATE);
		return ImmutableList.of(new DateRange(rule.getStartTime(), rule.getEndTime()));
	}
}
