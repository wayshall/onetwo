package org.onetwo.boot.limiter;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.boot.limiter.LimiterCreator.LimiterConfig;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.MapRegisterManager;




/**
 * @author wayshall
 * <br/>
 */
public class LimiterCreator extends MapRegisterManager<String, Function<LimiterConfig, InvokeLimiter>> {
	public static final LimiterCreator INSTANCE = new LimiterCreator();
	
	private MatcherRegister matcherRegister = MatcherRegister.INSTANCE;
	
	public LimiterCreator() {
		super();
		register(LocalRateLimiter.class.getSimpleName(), (config)->{
			Matcher matcher = matcherRegister.createMatcher(config.getMatcher(), config.getPatterns());
			LocalRateLimiter limiter = new LocalRateLimiter(config.getKey(), matcher);
			limiter.setLimitTimes(config.getLimitTimes());
			return limiter;
		})
		.register(LocalIntervalLimiter.class.getSimpleName(), (config)->{
			Matcher matcher = matcherRegister.createMatcher(config.getMatcher(), config.getPatterns());
			LocalIntervalLimiter limiter = new LocalIntervalLimiter(config.getKey(), matcher);
			limiter.setLimitTimes(config.getLimitTimes());
			Pair<Integer, TimeUnit> interval = LangOps.parseTimeUnit(config.getInterval());
			limiter.setInterval(interval.getKey(), interval.getValue());
			return limiter;
		})
		;
	}

	
	@SuppressWarnings("unchecked")
	public <T extends InvokeLimiter> T createLimiter(LimiterConfig config){
		String limiterName = config.getLimiter();
		Assert.hasText(limiterName);
		InvokeLimiter limiter = findRegistered(limiterName).orElseThrow(()->new BaseException("Limiter not found, name: " + limiterName))
													.apply(config);
		limiter.init();
		return (T)limiter;
	}
	
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class LimiterConfig {
		String key;
		String limiter;
		String matcher;//: antpath
		String[] patterns;//: /uaa/**,/order/**
		String interval;//: 60s
		int limitTimes;//: 100 #限制调用次数
	}

}
