package org.onetwo.boot.module.cos;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class CosStsData {
	/***
	 * 时间戳，单位秒，如：1580000000
	 */
	Long startTime;
	Long expiredTime;
	String expiration;
	
	CredentialsData credentials;

	@Data
	public static class CredentialsData {
		String tmpSecretId;
		String tmpSecretKey;
		String sessionToken;
	}
}
