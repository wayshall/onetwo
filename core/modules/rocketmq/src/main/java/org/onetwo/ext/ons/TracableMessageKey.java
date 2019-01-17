package org.onetwo.ext.ons;

import lombok.Builder;
import lombok.Value;

/**
 * @author weishao zeng
 * <br/>
 */
@Value
@Builder
public class TracableMessageKey {
	/***
	 * 标识某用户某操作的身份key，不带时间戳
	 */
	String identityKey;
	/***
	 * 唯一key
	 */
	String key;
}

