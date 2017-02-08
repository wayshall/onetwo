# security模块
------
基于spring security实现的动态rbac和基于Java代码的权限管理   
交流群：  8060215

   

##maven
```xml

<dependency>
    <groupId>org.onetwo4j</groupId>
    <artifactId>onetwo-security</artifactId>
    <version>4.3.8-SNAPSHOT</version>
</dependency>
```

##启用
基于onetwo的项目，在Configuration类上加上@EnableOnetwoUrlSecurity注解，提供一个实现了接口RootMenuClassProvider的bean，和添加[web-admin插件](https://github.com/wayshall/onetwo/blob/master/core/plugins/web-admin)依赖。   
```java     
  
	@Configuration
	@EnableOnetwoUrlSecurity
	public class AppContextConfig {

		@Bean
		public RootMenuClassProvider menuConfig(){
			return ()->Apps.class;
		}
	
	}   
   
```

onetwo会自动集成动态rbac的相关配置。
  
如果是非onetwo的项目，则相对麻烦点：
- 添加@EnableOnetwoSecurity注解
- 实现了PermissionConfig接口，并注册到容器
- 实现PermissionManager接口，并注册到容器

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

##基于Java代码的权限管理 


**待续。。。**




