package org.onetwo.boot.module.activemq.jmx;

import org.onetwo.boot.module.activemq.ActivemqProperties;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(ActiveMQJmxServiceProperties.PREFIX_KEY)
public class ActiveMQJmxServiceProperties {
	
	public static final String PREFIX_KEY = ActivemqProperties.PREFIX_KEY + ".jmx";
	public static final String ENABLE_KEY = PREFIX_KEY + ".serviceUrl";
	
	String serviceUrl = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";
	String username;
	String password;
	
	public void check() {
		if (StringUtils.isBlank(serviceUrl)) {
			throw new BaseException("activemq jmx serviceUrl can not be blank!");
		}
		if (StringUtils.isBlank(username)) {
			throw new BaseException("activemq jmx username can not be blank!");
		}
	}

}
