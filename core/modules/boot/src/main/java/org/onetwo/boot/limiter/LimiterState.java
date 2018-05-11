package org.onetwo.boot.limiter;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@Data
@Builder
public class LimiterState implements Serializable {
//	long startMillis;
	/***
	 * 过期时间
	 */
	long expireAt;
	/***
	 * 剩余次数
	 */
	int remainTimes;
	
	long resetInMillis;
	
	public boolean isExpired(){
		return System.currentTimeMillis()>=expireAt;
	}

}
