package org.onetwo.boot.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * 把一些升级到spring boot 2.2.x后需要修复兼容到配置统一放到这里
 * @author weishao zeng
 * <br/>
 */
@Configuration
public class BootConfigurationForFixSpringBoot2x {

	/***
	 * spring boot 2.2.x 版本后默认不在启用此过滤器
	 * 这里设置为默认启用，兼容以前的代码
	 * @author weishao zeng
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}
}
