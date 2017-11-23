package org.onetwo.boot.core.embedded;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties("jfish.tomcat")
@Data
public class TomcatProperties {
	public static final String ENABLED_CUSTOMIZER_TOMCAT = "enabled";
	
	int backlog = 500;
	int maxConnections = -1;
	int connectionTimeout = 30000;

}
