package org.onetwo.boot.limiter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.boot.limiter.InvokeContext.DefaultInvokeContext;
import org.onetwo.boot.limiter.InvokeContext.InvokeType;

/**
 * @author wayshall
 * <br/>
 */
public class LocalRateLimiterTest {
	MatcherRegister matcherRegister = new MatcherRegister();
	
	@Test
	public void testLocalInvokeLimiter(){
		LocalInvokeLimiter limiter = new LocalInvokeLimiter();
		limiter.setMatcherName("antpath");
		limiter.setPatterns("/uaa/**", "/order/**");
		limiter.setLimitTimes(3);
		
		limiter.init();

		DefaultInvokeContext context = DefaultInvokeContext.builder()
															.requestPath("/uaa/user/info/1")
															.invokeType(InvokeType.BEFORE)
															.invokeTimes(1)
															.build();
		boolean match = limiter.match(context);
		assertThat(match).isTrue();
		limiter.consume(context);
		limiter.consume(context);
		limiter.consume(context);
		limiter.consume(context);
	}

}
