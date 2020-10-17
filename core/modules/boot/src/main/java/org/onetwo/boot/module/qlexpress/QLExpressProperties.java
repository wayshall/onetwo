package org.onetwo.boot.module.qlexpress;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@ConfigurationProperties(QLExpressProperties.PREFIX)
@Data
public class QLExpressProperties {
	public static final String PREFIX = BootJFishConfig.PREFIX + ".qlexpress";
	public static final String ENABLE_KEY = PREFIX + ".enabled";

	/**
	 * 是否输出所有的跟踪信息，同时还需要log级别是DEBUG级别
	 */
	boolean trace = false;

	/**
	 * 是否使用逻辑短路特性增强质量的效率
	 */
	boolean shortCircuit = true;

	/**
	 * 是否需要高精度计算
	 */
	boolean precise = true;
	boolean cache = true;
}
