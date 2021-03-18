package org.onetwo.boot.module.oauth2.restclient;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties.ClientDetailsResolverProps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@ConditionalOnProperty(name=ClientDetailsResolverProps.AUTHORIZATION_BASE_URL)
@Import(OAuthRestClientRegistar.class)
public class OAuthRestClientConfiguration {

}

