package org.onetwo.cloud.eureka;

import org.onetwo.cloud.core.BootJfishCloudConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableEurekaServer
@ConditionalOnProperty(name=BootJfishCloudConfig.EUREKA_EMBEDDED_KEY, havingValue="true", matchIfMissing=false)
public class EmbeddedEurekaServerConfiguration {

}

