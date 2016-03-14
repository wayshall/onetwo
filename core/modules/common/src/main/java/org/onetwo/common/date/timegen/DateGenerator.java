package org.onetwo.common.date.timegen;

import java.util.Collection;

import org.onetwo.common.date.DateRange;
import org.onetwo.common.date.timegen.TimeRule.RuleType;

public interface DateGenerator {
	public RuleType getRuleType();
	public Collection<DateRange> generate(TimeRule rule);
}
