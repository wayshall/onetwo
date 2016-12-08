package org.onetwo.boot.module.security.method;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.boot.module.permission.BootPermissionContextConfig;
import org.onetwo.ext.security.EnableSecurity;
import org.onetwo.ext.security.EnableSecurity.InterceptMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
//@Import({ BootMethodBasedSecurityConfig.class })
@Import({BootPermissionContextConfig.class})
@EnableSecurity(mode=InterceptMode.CUSTOM, configClass=BootMethodBasedSecurityConfig.class, enableJavaStylePermissionManage=false)
@Configuration
@EnableGlobalMethodSecurity
public @interface EnableMethodSecurity {
}
