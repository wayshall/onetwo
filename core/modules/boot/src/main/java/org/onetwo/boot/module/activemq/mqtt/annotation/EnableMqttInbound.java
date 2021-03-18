package org.onetwo.boot.module.activemq.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.module.activemq.mqtt.inbound.MqttInboundImportRegistrar;
import org.onetwo.common.apiclient.impl.RestApiClentRegistrar;
import org.springframework.context.annotation.Import;

/**
 * 
 * 扩展：<br/>
 * &#064;EnableRestApiClient<br/>
 * &#064;Configuration<br/>
 * class XxxxConfiguration {
 * }
 * <br/><br/>
 * 1，自定义@EnableXxxxApiClient和@XxxxApiClient <br/>
 * 2，自定XxxxApiClentRegistrar <br/>
 * 3，自定义XxxxApiClientFactoryBean <br/>
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MqttInboundImportRegistrar.class)
public @interface EnableMqttInbound {
	
	String[] basePackages() default {};
	Class<?>[] basePackageClasses() default {};
	
}
