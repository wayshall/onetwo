# cloud   

对spring cloud的一些扩展和封装
------

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
    <artifactId>onetwo-cloud</artifactId>
    <version>4.7.2-SNAPSHOT</version>
</dependency>

```
## 启动spring cloud扩展
```Java
@EnableJFishCloudExtension
public class Application{
}
```

## 扩展
- feign client的get方法调用可直接使用对象定义参数
- 可自定义feign client调用时传递当前请求头
- 重新定义了feign client的errorCoder，远程调用返回错误时，直接抛异常
- 支持feign client远程调用转本地调用
- feign接口支持直接在参数里定义MultipartFile对象


## 配置

### 启用feign日志配置
```yaml
jfish: 
    dynamic: 
        loggerLevel: true
    cloud: 
        feign: 
            logger: 
                level: BASIC
                autoChangeLevel: true #自动把相关的client的logger日志级别修改为debug
```

### 设置feignClient是否检测本地接口，设置true，如果检测到服务提供者为本地服务，则会把feign远程调用转为本地调用
```yaml
jfish: 
    cloud: 
        feign: 
            local: 
				enabled: true 
```

### 设置feignClient拒绝添加插件路径
```yaml
jfish: 
    cloud: 
        feign: 
            rejectPluginContextPath: true #设置feignClient拒绝添加插件路径，默认为true
```

### 配置okhttp
```yaml
jfish: 
    cloud: 
        feign: 
            okHttpClient: 
                readTimeout: 50s #50秒
                connectTimeout: 50s 
                writeTimeout: 50s 
```

### 当使用hystrix调用feign client的时候，hystrix线程隔离会导致feign拦截器无法获取当前上下文的spring mvc请求上下文，开启下面这个配置可共享当前请求的requestContext
```yaml

jfish: 
    cloud: 
        hystrix: 
            shareRequestContext: true
```

### 设置feign调用的时候把当前header传递给服务提供者，可使用下面配置，默认会传递两个头：Authorization和auth
```yaml
jfish: 
    cloud: 
        hystrix: 
            shareRequestContext: true
        feign: 
            keepHeaders: Authorization, auth, otherHeader1, otherHeader2
```


### oauth2配置
```yaml

jfish: 
    oauth2: 
        authorizationServer:
            requestMatchers: /oauth/** #配置拦截路径
            #anyRequest: permitAll
            allowFormAuthenticationForClients: true #配置是否允许client使用form认证，默认为http basic
            clientDetailStore: jdbc #clientdetail 存储方式
            checkTokenAccess: permitAll #checkTokenAccess权限

```

### 灰度发布支持
服务配置：
```yaml
eureka:
    instance:
        metadata-map:
            # x-client-tag
            canary.filters.client-tag: v1.3
jfish: 
    cloud: 
        hystrix: 
            shareRequestContext: true
```
调用的时候增加header：
x-canary: FORCE
x-client-tag: v1.3