# boot
------
基于spring boot的快速开发框架   

## 要求
JDK 1.8+
spring 4.0+

## maven
添加仓储：
```xml
<repositories>
	<repository>
	     <id>oss</id>
	     <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
	    <snapshots>
	        <enabled>true</enabled>
	    </snapshots>
	</repository> 
</repositories>
```
添加依赖：
```xml

<dependency>
    <groupId>org.onetwo4j</groupId>
    <artifactId>onetwo-boot</artifactId>
    <version>4.6.1-SNAPSHOT</version>
</dependency>

```

## 配置

### webapi配置
使用了@WebApi注解的controller，会被自动加上"/api"前缀
```yaml
jfish: 
    webApi:
        prefixPath: /api
```
- @WebApi可继承

### 是否启用插件配置：
默认启用
```yaml
jfish: 
    plugin: 
        OrderPlugin: #插件名称
            enabled: false
```

### tomcat配置
```yaml
jfish: 
	tomcat: 
		asyncTimeout: 60000 #异步超时
```
### 异步controller配置
```yaml
jfish:
	mvc: 
		async: 
			enabled: true
			corePoolSize: 5
			maxPoolSize: 50
			queueCapacity: 100000
```

### 异步任务配置
```yaml
jfish:
	async: 
		enabled: true
		corePoolSize: 5
		maxPoolSize: 50
		queueCapacity: 100000
```
### 阿里ons配置
注解启用
```Java
@EnableONSClient(producers={
		@ONSProducer(producerId="PRODUCER_ID")
})
```
配置文件：
```yaml
jfish: 
    ons: 
        accessKey: 
        secretKey: 
        onsAddr: http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet

```


### 阿里oss上传支持
注入上传service
```Java
BootCommonService.upload
```
配置文件：
```yaml
site: 
    upload: 
        storeType: alioss
jfish: 
    alioss: 
        bucketName: 
        endpoint: 
        accessKeyId: 
        accessKeySecret: 

```

### jwt配置
```yaml
jfish:
    jwt: 
        enable: true
        signingKey: 随机字符串
        expirationInSeconds: 3600

```

### 动态数据源
```yaml
jfish: 
    dsRouter: 
        enabled: true
        lookupStrategy: header #查找策略，从header中读取X-Request-Datasource
        common: 
            driverClassName: com.mysql.jdbc.Driver
            validationQuery: SELECT 1
            testOnBorrow: true
            testOnReturn: true
        targets: 
            ds1: 
                url: 
                username: 
                password: 
                default: true
            ds2: 
                url: 
                username: 
                password: 
            ds3: 
                url: 
                username: 
                password: 
```
### apiclient配置
当使用@EnableRestApiClient 激活apiclient模块时，可以配置如下选项：
```yaml
jfish: 
	apiclient: 
		connectTimeout:
		readTimeout: 
		writeTimeout: 
```

### security
注意先屏蔽boot自动配置是security：
```xml
security: 
    ignored: /**
    basic: 
        enabled: false
```

内存用户配置：
```yaml
jfish: 
    security: 
        jwt: #使用jwt
            signingKey: testkey
            authStore: COOKIES
        csrf: 
            disable: true
        loginUrl: ${jfish.security.defaultLoginPage}
        metadataSource: none
        defaultLoginPage: /login #配置spring security的自带登录页路径
        memoryUsers: 
            admin: 
                roles: ADMIN
        anyRequest: none #不配置anyRequest，默认为：authenticated
		# requestMatchers: /**
        alwaysUseDefaultTargetUrl: false
        intercepterUrls: 
            /**/management/** : hasRole('ROLE_ADMIN')
            /login/** : permitAll
```

### oauth2配置：
#### 认证服务器：
Java配置
```Java
@EnableJFishOauth2(OAuth2Role.AUTHORIZATION_SERVER)
```
```yaml
jfish: 
    oauth2: 
        jwt: 
            signingKey: testkey
        tokenStore: jwt #redis #token存储方式
        authorizationServer:
            anyRequest: permitAll
            allowFormAuthenticationForClients: true #允许form认证
            clientDetailStore: jdbc #client存储方式
            checkTokenAccess: permitAll #check_token端点权限

```
通过：http://localhost:8080/oauth/token 获取access_token
参数：
grant_type = client_credentials
client_id = test
client_secret = test


#### 资源服务器：
Java配置
```Java
@EnableJFishOauth2(OAuth2Role.RESOURCE_SERVER)
```
yaml配置
```yaml

jfish: 
    oauth2: 
        jwt: 
            signingKey: testkey
        tokenStore: jwt #redis
        resourceServer: 
            # anyRequest: none
            requestMatchers: /api/**
            intercepterUrls: 
                /oauth/token : permitAll
                /api/** : authenticated
```
访问资源时带上access_token
http://localhost:8081/api/article?access_token=xxxxxxxxxxxxx

#### 增加jwt-redis tokenStore
这种存储方式结合redis和jwt的形式，认证服务器把jwt的token和uuid对应保存到redis，客户端使用uuid作为access_token，网关统一验证access_token并转为jwt的token，网关后面的服务使用jwt的token来调用，从而实现无状态调用，又避免了jwt的token无法手动失效的缺点。

### 优雅退出支持
```yaml
jfish: 
	graceKill: 
		enabled: true
```
kill -12 pid

### accessLog
```yaml
jfish:
    mvc: 
        accessLog: 
            enabled: true
            pathPatterns: /health
```

### redis
配置redisTemplate的序列化方式为jackson2
```yaml
jfish:
    redis: 
        serializer: jackson2
```

### redis cache
redis 缓存配置
使用json序列化缓存
```yaml
jfish:
    cache: 
        redis: 
			useJsonRedisTemplate: true
            usePrefix: true
            expires:
                key1:key2: 30m
```

### dbm
是否启用dbm sql统计controller
```yaml
jfish: 
    dbm: 
        web: 
            statisController: 
                enabled: true
```

### cors配置
```yaml
jfish: 
	cors: 
        - mapping: /v2/api-doc
          allowedOrigins: *
		  allowedMethods: GET, OPTION
		  allowedHeaders: Content-Type
```

### 其它一些简化使用的扩展

- @BootMvcArgumentResolver 注解   
所有标注了@BootMvcArgumentResolver 注解的Bean被自动注册为spring mvc的参数解释器

- @Interceptor 注解   
JFish自定义的拦截器机制，可以直接在Controller的类或者方法上使用@Interceptor 注解，标注该方法的请求必须经过指定的拦截器，简化Spring mvc的拦截器使用。拦截器类必须实现 MvcInterceptor 接口。
支持重复和嵌套，如：
```Java
@Interceptor(MvcInterceptor1.class)
@Interceptor(MvcInterceptor2.class)
public static @interface CombinationMVCInterceptor {
}

@CombinationMVCInterceptor
public class HomeController {
}
```
- 增加注解+接口风格的RestClient机制，可通过@EnableRestApiClient启用

- 增加@OnMissingBean注解，类似boot的@ConditionalOnMissingBean

- 增加@OnExistingBean 注解，类似boot的@ConditionalOnBean

- 增加@WebApi 注解，在controller前加前缀
- 使用@UploadFileValidator 注解，限制和验证上传文件的后缀和大小