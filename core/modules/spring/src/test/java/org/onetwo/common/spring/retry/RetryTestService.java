package org.onetwo.common.spring.retry;

import org.onetwo.common.date.DateUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public class RetryTestService {
	
	private int count = 0;
	private int successCount = 3;
	
	/***
	 * multiplier 倍数递增
	 * 0: 0
	 * 1: 2
	 * 2: 2*3
	 * 3: 2*3*3
	 * 4: 2*3*3*3
	 * @return
	 */
	@Retryable(maxAttempts=5, backoff=@Backoff(delay=2000, multiplier=3))
	public int saySomething(){
		String dateStr = DateUtils.nowString();
		if(true){
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
