package org.onetwo.boot.plugins.security.method;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.boot.plugins.security.config.MethodSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({ MethodSecurityConfig.class })
@Configuration
public @interface EnableJFishMethodSecurity {
}
