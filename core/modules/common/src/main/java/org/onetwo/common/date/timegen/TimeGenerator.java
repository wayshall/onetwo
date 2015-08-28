package org.onetwo.common.date.timegen;

import java.util.Collection;

import org.onetwo.common.date.DateRange;

public interface TimeGenerator {

	TimeGenerator registerOrOver(DateGenerator g);

	TimeGenerator register(DateGenerator g);

	Collection<DateRange> generate(TimeRule rule);

}