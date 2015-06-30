package org.onetwo.boot.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true, proxyTargetClass=true)
public class MethodSecurityConfig {

}
