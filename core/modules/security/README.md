# security模块
------
基于spring security实现的动态rbac和基于Java代码的权限管理   
联系邮箱：  wayshall@qq.com

   

## maven
```xml

<dependency>
    <groupId>org.onetwo4j</groupId>
    <artifactId>onetwo-security</artifactId>
    <version>4.6.1-SNAPSHOT</version>
</dependency>
```

## 启用
基于onetwo的项目，在Configuration类上加上 @EnableUrlSecurity 注解，提供一个实现了接口RootMenuClassProvider的bean，和添加[web-admin插件](https://github.com/wayshall/onetwo/blob/master/core/plugins/web-admin)依赖。   
```java     
  
	@Configuration
	@EnableUrlSecurity
	public class AppContextConfig {

		@Bean
		public RootMenuClassProvider menuConfig(){
			return ()->Apps.class;
		}
	
	}   
   
```

jfish 会自动集成动态rbac的相关配置。
  
如果是非 jfish 的项目，则相对麻烦点：
- 添加 @EnableSecurity 注解
- 实现了 PermissionConfig 接口，并注册到容器
- 实现 PermissionManager 接口，并注册到容器

```java     
  
	@Configuration
	@EnableOnetwoUrlSecurity
	public class AppContextConfig {

		@Bean
		public PermissionConfig menuConfig(){
			……
		}

		@Bean
		public PermissionManager permissionManager(){
			……
		}
	
	}   
   
```

## 基于Java代码的权限管理 


## 配置

### 完整示例配置
```yaml
jfish: 
    security: 
        # redirectStrategy: 
        #    forceHttps: true
        #    httpsPort: 443
        csrf: 
            disable: true
        loginUrl: ${jfish.security.defaultLoginPage}
        metadataSource: none #jfish默认会使用database，基于数据库的权限管理，否则设置为none
        defaultLoginPage: /login #设置security默认登录页路径
        memoryUsers: #配置基于内存的用户角色
            admin: #userName
                roles: ADMIN
        anyRequest: none
        alwaysUseDefaultTargetUrl: true
        afterLoginUrl: http://localhost/user/center #登录成功后跳转
        intercepters: 
            -
                pathPatterns: /authorize/** 
                access: hasRole('ROLE_ADMIN')
        jwt: 
            signingKey: # 随机字符串
            authStore: COOKIES
```

**待续。。。**




