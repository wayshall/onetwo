package org.onetwo.common.date.timegen;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.date.DateRange;
import org.onetwo.common.date.timegen.TimeRule.RuleType;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

public class DefaultTimeGenerator implements TimeGenerator {
	
	private Map<RuleType, DateGenerator> dateMaps = LangUtils.newHashMap();
	
	public void registerDefaultGenerators(){
		this.register(new ExactDateGenerator());
		this.register(new DateOfWeekCycleGenerator());
		this.register(new DateOfMonthCycleGenerator());
	}

	@Override
	final public TimeGenerator registerOrOver(DateGenerator g){
		dateMaps.put(g.getRuleType(), g);
		return this;
	}
	@Override
	final public TimeGenerator register(DateGenerator g){
		if(dateMaps.containsKey(g.getRuleType())){
			throw new BaseException("date generator has register for time rule: " + g.getRuleType());
		}
		dateMaps.put(g.getRuleType(), g);
		return this;
	}

	@Override
	public Collection<DateRange> generate(TimeRule rule){
		Assert.notNull(rule.getRuleType());
		DateGenerator g = dateMaps.get(rule.getRuleType());
		if(g==null){
			throw new BaseException("no generator found for time rule: " + rule.getRuleType());
		}
		return g.generate(rule);
	}
}
