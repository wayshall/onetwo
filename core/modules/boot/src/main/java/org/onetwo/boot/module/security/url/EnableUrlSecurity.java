package org.onetwo.boot.module.security.url;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.boot.module.permission.BootPermissionContextConfig;
import org.onetwo.ext.security.EnableSecurity;
import org.onetwo.ext.security.EnableSecurity.ConfigOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/****
 * 因为现在默认是使用数据库保存资源权限，一个不使用数据保存这些数据的典型配置如下：
 * 
jfish: 
    security: 
        metadataSource: none
        defaultLoginPage: true
        memoryUsers: 
            admin: 
                roles: ADMIN
        anyRequest: none
        alwaysUseDefaultTargetUrl: false
        ignoringUrls: /api/**
        intercepterUrls: 
            /jfish/management/** : hasRole('ROLE_ADMIN')
 * @author wayshall
 *
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
//@Import({ BootUrlBasedSecurityConfig.class })
@Import({BootPermissionContextConfig.class})
@EnableSecurity(mode=ConfigOptions.CUSTOM, configClass={BootUrlBasedSecurityConfig.class}, enableJavaStylePermissionManage=false)
@Configuration
public @interface EnableUrlSecurity {
}
