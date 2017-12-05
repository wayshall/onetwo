# boot
------
基于spring boot的快速开发框架   
交流群：  604158262

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
    <artifactId>onetwo-spring</artifactId>
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

### 其它一些简化使用的扩展

- @BootMvcArgumentResolver 注解
所有标注了@BootMvcArgumentResolver 注解的Bean被自动注册为spring mvc的参数解释器

- @Interceptor 注解
可以直接在Controller的类或者方法上使用@Interceptor 注解，标注该方法的请求必须经过指定的拦截器，简化Spring mvc的拦截器使用。拦截器类必须实现 MvcInterceptor 接口

- 增加注解+接口风格的RestClient机制，可通过@EnableRestApiClient启用

- 增加@OnMissingBean注解，类似boot的@ConditionalOnMissingBean

- 增加@OnExistingBean 注解，类似boot的@ConditionalOnBean

- 增加@WebApi 注解，在controller前加前缀