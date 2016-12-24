package org.onetwo.common.spring.retry;

import org.onetwo.common.date.DateUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public class RetryTestService {
	
	private int count = 0;
	private int successCount = 3;
	
	/***
	 * multiplier 倍数递增
	 * @return
	 */
	@Retryable(maxAttempts=4, backoff=@Backoff(delay=1000, multiplier=2))
	public int saySomething(){
		String dateStr = DateUtils.nowString();
		if(count<successCount){
			System.out.println(dateStr+": error count: " +count);
			count++;
			throw new RuntimeException("error");
		}
		System.out.println(dateStr+": say hello success!");
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

}
