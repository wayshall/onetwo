package org.onetwo.common.timegen;

import org.junit.Test
import org.onetwo.common.timegen.TimeRule.RuleType

public class TimeGenTest {
	
	@Test
	public void test(){
		TimeGenerator tg = new Timegenerator();
		TimeRule rule = new TimeRule();
		rule.ruleType = RuleType.EXACT_DATE;
		rule.startIndex = 
		tg.generate(rule)
	}

}
