package org.onetwo.common.timegen;

import java.util.Collection;

import org.onetwo.common.timegen.TimeRule.RuleType;

public interface DateGenerator {
	public RuleType getRuleType();
	public Collection<DateRange> generate(TimeRule rule);
}
