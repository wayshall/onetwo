package org.onetwo.ext.security.utils;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class ExceptionUserCheckerConfig {
	private String duration = "30m";
	private int maxLoginTimes = 5;
}
