package org.onetwo.cloud;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.cloud.feign.ExtFeignConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({EnableJFishCloudExtensionSelector.class})
@EnableFeignClients(defaultConfiguration=ExtFeignConfiguration.class)
public @interface EnableJFishCloudExtension {
}
