package org.onetwo.boot.limiter;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
public interface InvokeLimiter {
	
	String getKey();
	
	default void init(){};

	boolean match(InvokeContext invokeContext);
	
	void consume(InvokeContext invokeContext);
	
	public abstract class AbstractInvokeLimiter implements InvokeLimiter {
//		private MatcherRegister matcherRegister = new MatcherRegister();
		/*private String matcherName;
		private String[] patterns;*/
		protected final Logger logger = JFishLoggerFactory.getLogger(InvokeLimiter.class);
		
		private String key;
		private InvokeType invokeType;
		private Matcher matcher;
		
		public AbstractInvokeLimiter(String key, InvokeType invokeType,
				Matcher matcher) {
			super();
			this.key = key;
			this.invokeType = invokeType;
			this.matcher = matcher;
		}

		@Override
		public void init() {
			Assert.notNull(key, "key can not be empty");
			Assert.notNull(invokeType, "invokeType can not be empty");
			Assert.notNull(matcher, "matcher can not be empty");
//			this.setInvokeType(invokeType);
			/*if(StringUtils.isNotBlank(matcherName)){
				Assert.notEmpty(patterns, "patterns can not be empty");
				this.setMatcher(matcherRegister.createMatcher(matcherName, patterns));
			}*/
		}
		
		public boolean match(InvokeContext invokeContext){
			return (getInvokeType()==InvokeType.ALL || getInvokeType()==invokeContext.getInvokeType()) && isMatcherMatch(invokeContext);
		}
		
		private boolean isMatcherMatch(InvokeContext invokeContext){
			return getMatcher().matches(invokeContext);
		}

		public Matcher getMatcher() {
			return matcher;
		}

		public void setMatcher(Matcher matcher) {
			this.matcher = matcher;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public InvokeType getInvokeType() {
			return invokeType;
		}
		public void setInvokeType(InvokeType invokeType) {
			this.invokeType = invokeType;
		}

		/*public String getMatcherName() {
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
		}*/
	}
	
	public abstract class TimesInvokeLimiter extends AbstractInvokeLimiter {

		private int limitTimes;

		public TimesInvokeLimiter(String key, InvokeType invokeType, Matcher matcher) {
			super(key, invokeType, matcher);
		}
		
		public int getLimitTimes() {
			return limitTimes;
		}

		public void setLimitTimes(int limitTimes) {
			this.limitTimes = limitTimes;
		}
	}
}
