package org.onetwo.common.utils.timegen;

import java.util.List;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DateRange;
import org.onetwo.common.utils.timegen.TimeRule.RuleType;

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
