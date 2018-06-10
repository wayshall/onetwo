package org.onetwo.boot.limiter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.LimiterCreator.LimiterConfig;
import org.onetwo.common.utils.LangOps;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
public class LimiterManager {
	
	private LimiterCreator limiterCreator = LimiterCreator.INSTANCE;
	private List<LimiterConfig> limiterConfigs;
	private Map<String, InvokeLimiter> limiters;
	
	public Map<String, InvokeLimiter> buildLimiter(){
		Assert.notEmpty(limiterConfigs, "limiterConfigs can not be empty");
		limiters = limiterConfigs.stream().map(config->{
			InvokeLimiter limiter = limiterCreator.createLimiter(config);
//			limiter.init();
			return limiter;
		})
		.collect(Collectors.toMap(limiter->limiter.getKey(), limiter->limiter, LangOps.throwingMerger(), LinkedHashMap::new));
		return limiters;
	}
	
	public List<InvokeLimiter> findLimiters(InvokeType invokeType){
		return limiters.values()
						.stream()
						.filter(limiter->limiter.getInvokeType()==invokeType)
						.collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends InvokeLimiter> Optional<T> find(String key){
		return Optional.ofNullable((T)this.limiters.get(key));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends InvokeLimiter> T get(String key){
		return (T)find(key).orElseThrow(()->new NoSuchElementException("limiter not found for key : " + key));
	}

	public Map<String, InvokeLimiter> getLimiters() {
		return limiters;
	}

	public void setLimiterCreator(LimiterCreator limiterCreator) {
		this.limiterCreator = limiterCreator;
	}

	public void setLimiterConfigs(LimiterConfig... limiterConfigs) {
		this.limiterConfigs = Arrays.asList(limiterConfigs);
	}

	public void setLimiterConfigs(List<LimiterConfig> limiterConfigs) {
		this.limiterConfigs = limiterConfigs;
	}

}
