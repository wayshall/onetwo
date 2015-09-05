package org.onetwo.boot.core.config;

import org.onetwo.common.propconf.JFishProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * 需要使用到的bussiness的配置
 * @author way
 *
 */
@SuppressWarnings("serial")
@ConfigurationProperties(prefix="bussiness")
public class BootBussinessConfig extends JFishProperties {
	
	@Autowired
	private BootSpringConfig bootSpringConfig;

	public BootSpringConfig getBootSpringConfig() {
		return bootSpringConfig;
	}
	
}
