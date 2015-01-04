package org.onetwo.common.utils.timegen;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DateRange;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.timegen.TimeRule.RuleType;

public class Timegenerator {
	
	private Map<RuleType, DateGenerator> dateMaps = LangUtils.newHashMap();

	public Timegenerator registerOrOver(DateGenerator g){
		dateMaps.put(g.getRuleType(), g);
		return this;
	}
	public Timegenerator register(DateGenerator g){
		if(dateMaps.containsKey(g.getRuleType())){
			throw new BaseException("date generator has register for time rule: " + g.getRuleType());
		}
		dateMaps.put(g.getRuleType(), g);
		return this;
	}

	public Collection<DateRange> generate(TimeRule rule){
		Assert.notNull(rule.getRuleType());
		DateGenerator g = dateMaps.get(rule.getRuleType());
		if(g==null){
			throw new BaseException("no generator found for time rule: " + rule.getRuleType());
		}
		return g.generate(rule);
	}
}
