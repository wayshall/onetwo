package org.onetwo.boot.module.security.method;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.boot.module.permission.BootPermissionContextConfig;
import org.onetwo.ext.security.EnableSecurity;
import org.onetwo.ext.security.EnableSecurity.ConfigOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
//@Import({ BootMethodBasedSecurityConfig.class })
@Import({BootPermissionContextConfig.class})
@EnableSecurity(mode=ConfigOptions.CUSTOM, configClass=BootMethodBasedSecurityConfig.class, enableJavaStylePermissionManage=false)
@Configuration
//@EnableGlobalMethodSecurity //此注解必须写在GlobalMethodSecurityConfiguration子类，否则无法起作用
public @interface EnableMethodSecurity {
}
