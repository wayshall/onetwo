package org.onetwo.boot.func.submit;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 限流器
 * @author weishao zeng
 * <br/>
 */

public interface RedisRateLimiter {

	/****
	 * 这个方法是限制前端表单提交的频率
	 * 直接限制为每5秒只能操作一次
	 * @author weishao zeng
	 * @param actionKey
	 *  这个方法是限制前端表单提交的频率
	 */
	default public void checkAction(String actionKey) {
		ActionContext actionContext = new ActionContext();
		actionContext.setActionKey(actionKey);
		actionContext.setTimesInPeriod(1);
		actionContext.setPeriod(5);
		actionContext.setErrorMessage("请勿频繁提交！");
		actionContext.setPeriodUnit(TimeUnit.SECONDS);
		consumeAction(actionContext);
	}

	default public void checkAction(String actionKey, int time, TimeUnit timeUnit) {
		checkAction(actionKey, time, timeUnit, "请勿频繁提交！");
	}
	
	default public void checkAction(String actionKey, int time, TimeUnit timeUnit, String errMsg) {
		ActionContext actionContext = new ActionContext();
		actionContext.setActionKey(actionKey);
		actionContext.setTimesInPeriod(1);
		actionContext.setPeriod(time);
		actionContext.setErrorMessage(errMsg);
		actionContext.setPeriodUnit(timeUnit);
		consumeAction(actionContext);
	}

	void consumeAction(ActionContext actionContext);
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ActionContext {
		
		/***
		 * 若要限制某个方法某个用户一个周期内只能调用1次
		 * 则actionKey=方法的key+用户id
		 */
		String actionKey;
		/***
		 * 周期，默认为：秒
		 */
		int period;
		
		/***
		 * 每个周期允许操作的次数
		 */
		int timesInPeriod;
		
		/***
		 * 周期时间单位
		 */
		@Builder.Default
		TimeUnit periodUnit = TimeUnit.SECONDS;

		@Builder.Default
		String errorMessage = "It's too frequent";
		
		// 根据开始和结束时间计算
		Date startTime;
		Date endTime;
//		String timePattern;
		
	}

}