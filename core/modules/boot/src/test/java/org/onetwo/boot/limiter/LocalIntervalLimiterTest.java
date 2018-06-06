package org.onetwo.boot.limiter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.onetwo.boot.limiter.InvokeContext.DefaultInvokeContext;
import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.LangUtils;

/**
 * @author wayshall
 * <br/>
 */
public class LocalIntervalLimiterTest {
	MatcherRegister matcherRegister = new MatcherRegister();
	
	@Test
	public void test(){
		LocalIntervalLimiter limiter = new LocalIntervalLimiter();
		limiter.setMatcherName("serviceId");
		limiter.setPatterns("user-service", "product-service");
		limiter.setLimitTimes(10);
		limiter.setInterval(5, TimeUnit.SECONDS);
		
		limiter.init();
		
		DefaultInvokeContext context = DefaultInvokeContext.builder()
															.requestPath("/order/1")
															.serviceId("order-service")
															.invokeType(InvokeType.BEFORE)
															.invokeTimes(1)
															.build();
		boolean match = limiter.match(context);
		assertThat(match).isFalse();
		
		final DefaultInvokeContext finalContext = DefaultInvokeContext.builder()
						.requestPath("/uaa/user/info/1")
						.serviceId("user-service")
						.invokeType(InvokeType.BEFORE)
						.invokeTimes(1)
						.build();
		match = limiter.match(finalContext);
		assertThat(match).isTrue();
		
		LangOps.ntimesRun(10, ()->{
			limiter.consume(finalContext);
		});

		assertThatExceptionOfType(LimitInvokeException.class).isThrownBy(()->{
			limiter.consume(finalContext);
		})
		.withMessage("exceed max limit invoke: "+limiter.getLimitTimes());
		
		LangUtils.await(5);
		LangOps.ntimesRun(10, ()->{
			limiter.consume(finalContext);
		});
		
		//ConcurrentRunnable
	}

}
