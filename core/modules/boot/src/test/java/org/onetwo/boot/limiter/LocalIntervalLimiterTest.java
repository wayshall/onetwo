package org.onetwo.boot.limiter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;
import org.onetwo.boot.limiter.InvokeContext.DefaultInvokeContext;
import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.LimiterCreator.LimiterConfig;
import org.onetwo.common.concurrent.ConcurrentRunnable;
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
		/*LocalIntervalLimiter limiter = new LocalIntervalLimiter();
		limiter.setMatcherName("serviceId");
		limiter.setPatterns("user-service", "product-service");
		limiter.setLimitTimes(10);
		limiter.setInterval(5, TimeUnit.SECONDS);*/
		
		LimiterConfig config = LimiterConfig.builder()
											.key("LocalIntervalLimiterTest")
											.matcher("serviceId")
											.patterns(new String[]{"user-service", "product-service"})
											.limitTimes(10)
											.interval("5s")
											.limiter(LocalIntervalLimiter.class.getSimpleName())
											.build();
		LocalIntervalLimiter limiter = LimiterCreator.INSTANCE.createLimiter(config);
		
		limiter.init();
		
		testLimiter(limiter);
		
		limiter.setLimiterStateCreator(l->{
			return new BetterLimiterState(l.getIntervalInMillis(), l.getLimitTimes());
		});
		limiter.init();
		testLimiter(limiter);
	}

	
	private void testLimiter(LocalIntervalLimiter limiter){

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
		.withMessage("limiter["+limiter.getKey()+"]: exceed max limit.");
		
		//等待五秒后清除状态
		LangUtils.await(5);
		LangOps.ntimesRun(10, ()->{
			limiter.consume(finalContext);
		});
		
		//重新初始化
		limiter.init();
		//十个线程并发
		ConcurrentRunnable.create(10, ()->{
			limiter.consume(finalContext);
		})
		.start()
		.await();
		assertThatExceptionOfType(LimitInvokeException.class).isThrownBy(()->{
			limiter.consume(finalContext);
		})
		.withMessage("limiter["+limiter.getKey()+"]: exceed max limit.");
	}
}
