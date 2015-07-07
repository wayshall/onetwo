package org.onetwo.boot.plugins.security;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish.security")
public class BootSecurityConfig {

	@Getter @Setter
	private ServiceProperties serviceProperties;

	public class ServiceProperties {
		@Getter @Setter
		private String service;
		@Getter @Setter
		private boolean sendRenew;
	}
}
