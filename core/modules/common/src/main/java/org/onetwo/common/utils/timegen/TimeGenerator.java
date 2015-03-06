package org.onetwo.common.utils.timegen;

import java.util.Collection;

import org.onetwo.common.utils.DateRange;

public interface TimeGenerator {

	TimeGenerator registerOrOver(DateGenerator g);

	TimeGenerator register(DateGenerator g);

	Collection<DateRange> generate(TimeRule rule);

}