package org.onetwo.boot.core.config;

import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * 某些专属jfish的配置
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish")
public class JFishBootConfig {
	
	@Setter
	@Getter
	private String ftlDir = "/jfish/ftl";

	@Setter
	@Getter
	private Properties mediaTypes;
	
}
