package org.onetwo.boot.limiter;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;

/**
 * @author wayshall
 * <br/>
 */
public interface InvokeLimiter {
	
	default void init(){};

	boolean match(InvokeContext invokeContext);
	
	void consume(InvokeContext invokeContext);
	
	public abstract class AbstractInvokeLimiter implements InvokeLimiter {
		private String key;
		private Matcher matcher;
		private int limitTimes;
		
		public boolean match(InvokeContext invokeContext){
			return getMatcher().matches(invokeContext);
		}

		public Matcher getMatcher() {
			return matcher;
		}

		public void setMatcher(Matcher matcher) {
			this.matcher = matcher;
		}

		public int getLimitTimes() {
			return limitTimes;
		}

		public void setLimitTimes(int limitTimes) {
			this.limitTimes = limitTimes;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
		
	}
	
	public abstract class BaseInvokeLimiter extends AbstractInvokeLimiter {
		private MatcherRegister matcherRegister = new MatcherRegister();
		private InvokeType invokeType;
		private String matcherName;
		private String[] patterns;

		@Override
		public void init() {
			super.init();
			this.setInvokeType(invokeType);
			this.setMatcher(matcherRegister.createMatcher(matcherName, patterns));
		}
		
		public boolean match(InvokeContext invokeContext){
			return (getInvokeType()==InvokeType.ALL || getInvokeType()==invokeContext.getInvokeType()) && super.match(invokeContext);
		}

		public InvokeType getInvokeType() {
			return invokeType;
		}
		public void setInvokeType(InvokeType invokeType) {
			this.invokeType = invokeType;
		}

		public String getMatcherName() {
			return matcherName;
		}

		public void setMatcherName(String matcherName) {
			this.matcherName = matcherName;
		}

		public String[] getPatterns() {
			return patterns;
		}

		public void setPatterns(String... patterns) {
			this.patterns = patterns;
		}
	}
}
