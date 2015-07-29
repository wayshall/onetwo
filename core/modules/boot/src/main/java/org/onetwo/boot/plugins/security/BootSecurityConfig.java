package org.onetwo.boot.plugins.security;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish.security")
@Data
public class BootSecurityConfig {

	private CasConfig cas = new CasConfig();


	@Data
	public class CasConfig {
		private String loginUrl;
		private String service;
		private boolean sendRenew = true;
		private String casServerUrl;
		private String key = CasConfig.class.getName();
	}
}
