package org.onetwo.boot.plugins.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/***
 * TODO
 * not used
 * 
 * @author way
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true, proxyTargetClass=true)
public class MethodSecurityConfig {

}
