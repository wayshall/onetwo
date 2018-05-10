package org.onetwo.boot.limiter;

import java.util.stream.Stream;

import org.onetwo.common.utils.Assert;

/**
 * @author wayshall
 * <br/>
 */
public interface Matcher {
	
	boolean matches(InvokeContext context);
	
	abstract public class AbstractMathcer implements Matcher {
		final private String[] patterns;
		
		public AbstractMathcer(String[] patterns) {
			super();
			Assert.notEmpty(patterns);
			this.patterns = patterns;
		}
		@Override
		public boolean matches(InvokeContext context) {
			return Stream.of(getPatterns())
							.anyMatch(pattern->doMatches(pattern, context));
//			return antMatcher.match(getPatterns(), context.getRequestPath());
		}
		
		protected boolean doMatches(String pattern, InvokeContext context){
			throw new RuntimeException("operation not implement yet!");
		}
		
		public String[] getPatterns() {
			return patterns;
		}
	}

}
