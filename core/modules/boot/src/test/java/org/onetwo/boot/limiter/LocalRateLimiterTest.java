package org.onetwo.boot.limiter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;
import org.onetwo.boot.limiter.InvokeContext.DefaultInvokeContext;
import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.LimiterRegister.LimiterConfig;
import org.onetwo.common.utils.LangUtils;

/**
 * @author wayshall
 * <br/>
 */
public class LocalRateLimiterTest {
	
	@Test
	public void testLocalInvokeLimiter(){
		/*LocalRateLimiter limiter = new LocalRateLimiter();
		limiter.setMatcherName("antpath");
		limiter.setPatterns("/uaa/**", "/order/**");
		*//***
		 * 每秒钟三次，记住是指速率rate，不是许可，如果太快，连续三次也是会失败的，中间必须休眠
		 *//*
		limiter.setLimitTimes(3);*/
		

		LimiterConfig config = LimiterConfig.builder()
											.key("LocalIntervalLimiterTest")
											.matcher("antpath")
											.patterns(new String[]{"/uaa/**", "/order/**"})
											//每秒钟三次，记住是指速率rate，不是许可，如果太快，连续三次也是会失败的，中间必须休眠
											.limitTimes(3)
											.limiter(LocalRateLimiter.class.getSimpleName())
											.build();
		LocalRateLimiter limiter = LimiterRegister.INSTANCE.createLimiter(config);
		
		limiter.init();

		DefaultInvokeContext context = DefaultInvokeContext.builder()
															.requestPath("/uaa/user/info/1")
															.invokeType(InvokeType.BEFORE)
															.invokeTimes(1)
															.build();
		boolean match = limiter.match(context);
		assertThat(match).isTrue();
		limiter.consume(context);
		LangUtils.awaitInMillis(400);
		limiter.consume(context);
		LangUtils.awaitInMillis(330);
		limiter.consume(context);
		
		assertThatExceptionOfType(LimitInvokeException.class).isThrownBy(()->{
			limiter.consume(context);
			limiter.consume(context);
		})
		.withMessage("limiter["+limiter.getKey()+"]: exceed max limit invoke: "+limiter.getLimitTimes());
	}

}
