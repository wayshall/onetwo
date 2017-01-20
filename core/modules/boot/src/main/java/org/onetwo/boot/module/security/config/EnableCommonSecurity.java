package org.onetwo.boot.module.security.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.ext.security.EnableSecurity;
import org.onetwo.ext.security.EnableSecurity.ConfigOptions;
import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.springframework.context.annotation.Configuration;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@EnableSecurity(mode=ConfigOptions.CUSTOM, configClass={SecurityCommonContextConfig.class,
														BootSecurityCommonContextConfig.class}, 
enableJavaStylePermissionManage=false)
@Configuration
public @interface EnableCommonSecurity {
}
