package org.onetwo.common.utils.timegen;

import java.util.Collection;

import org.onetwo.common.utils.DateRange;
import org.onetwo.common.utils.timegen.TimeRule.RuleType;

public interface DateGenerator {
	public RuleType getRuleType();
	public Collection<DateRange> generate(TimeRule rule);
}
