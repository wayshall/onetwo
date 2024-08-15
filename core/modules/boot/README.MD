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
	     <id>ossrh</id>
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
    <version>4.7.3-SNAPSHOT</version>
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

### 异常和http status code 映射配置
有些时候（设计），我们需要把一些我们业务或者框架抛出的异常映射为特定的http status code，比如200，可以这样配置：
```yaml
jfish: 
    mvc: 
        exceptionsStatusMapping: 
            TypeMismatchException: 200
            ConstraintViolationException: 200
            BindException: 200
            MethodArgumentNotValidException: 200
            "*": 400
```


### 导出excel

#### 使用xml模板导出
1、在/src/main/resources/excel-view/目录下创建xml模板：
```xml
<template name="订单明细表" columnWidth="0:20;2:20;3:20;5:20;7:20;8:20">
    <rows>
       <!-- CellStyle -->
        <row span="2" fieldStyle="alignment:ALIGN_CENTER;verticalAlignment:VERTICAL_CENTER;" fieldFont="boldweight:BOLDWEIGHT_BOLD">
            <fields>
                <field value="#title" rowspan="2" colspan="12"/>
            </fields>
        </row>
        <row renderHeader="true" 
              fieldHeaderStyle="alignment:ALIGN_CENTER;verticalAlignment:VERTICAL_CENTER;" fieldHeaderFont="boldweight:BOLDWEIGHT_BOLD"
              name="element" type="iterator" datasource="#datas" span="item.size()" fieldFont="boldweight:BOLDWEIGHT_NORMAL;"> 
            <fields>
                <field label="订单id" name="orderId" dataType="java.lang.String"/>
                <field label="用户姓名" name="nickname" dataType="java.lang.String"/>
                <field label="用户手机" name="userMobile"/>
                <field label="商品数量" name="itemCount"/>
                <field label="所属商户" name="storeName"/>
                <field label="订单总金额" name="totalAmount"/>
                <field label="支付时间" name="payTime" dataFormat="yyyy-MM-dd HH:mm:ss"/>
                <field label="菜品数量" name="skuCount" rootValue="item"/>
            </fields>
        </row>
    </rows>
</template>
```
2、在controller返回模板路径
```Java
    @GetMapping(path="export")
    public ModelAndView list(){
        return pluginMv("模板路径", "datas", datas);
    }
```
3、使用 .jfxls 后缀访问controller即可，框架会自动使用xml模板生成excel文件

#### 使用@ExcelEntity注解导出
使用注解的方式可以直接在原来页面查询的controller里实现，而无需重新写一个方法，但是这种导出目前有数量限制，最大一万条，而返回类型只能是List或者Page。
1、在返回的值类型里使用@ExcelEntity标注

2、在值类型里，把需要导出的字段或属性使用@ExcelExportable标注

3、如果是分页，不想只导出默认的一页20条数据，需要在controller里做一点小修改：
```Java
    @GetMapping("/export")
    public Page<Data> list(PageRequest pageRequest){
        Page<Data> page = null;
        if (isResponseJFxls()) {
            // 导出会执行此分支
            pageRequest.noLimited();
            page = service.findPage(pageRequest);
        } else {
            page = service.findPage(pageRequest);
        }
        return page;
    }
```

4、使用 .jfxls 后缀即可controller即可


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

### 多数据源
在application.yaml文件配置数据源连接信息：
```yaml
jfish: 
    datasources: 
        enabled: mysqlDatasource,postgreDatasource #这里配置要注册的数据源名称
        # 对应数据源详细配置
        mysqlDatasource: 
            driverClassName: com.mysql.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/mysqldb?&useSSL=false&characterEncoding=UTF-8
            username: mysqldb
            password: mysqldb
            testOnBorrow: true
            validationQuery: select 1
        postgreDatasource: 
            transactionManager: postgreTransactionManager #自定义对应事务管理器名称
            driverClassName: org.postgresql.Driver
            url: jdbc:postgresql://127.0.0.1:5432/postgre?useSSL=false&socketTimeout=30&connectTimeout=30
            username: postgre
            password: postgre
            testOnBorrow: true
            validationQuery: select 1
```
jfish会根据 jfish.datasources.enabled 配置的数据源名称自动寻找对应的连接配置注册数据源（没有配置在enabled的数据源则不会注册）。
同时会为每个数据源自动注册一个事务管理器，名称为：数据源名称+TransactionManager。
若需要自定义名称，则在数据源下配置transactionManager属性即可。
此名称可以用于使用spring的事务注解时，指定要提交的事务。

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

### OAuth2 sso客户端单点登录

#### 1、在项目下，新建一个配置类
```Java
@Configuration
@EnableOAuth2Sso
// 定制oauth错误异常
@Import(OAuth2CustomResultConfiguration.class)
public class OAuth2ClientSecurityConfigurer extends OAuth2SsoSecurityConfigurer {
    
    public OAuth2ClientSecurityConfigurer(AccessDecisionManager accessDecisionManager) {
        super(accessDecisionManager);
    }
    
}
```
#### 2、对应配置文件
```yaml
jfish:
    security: 
        failureUrl: http://localhost:9527/oauth2Login?error=true
    oauth2: 
        ssoclient: 
            stateMandatory: false
            tokenInfo.enabled: true #此属性会激活 SsoClientCustomTokenInfoUriConfiguration 配置类
security: 
    oauth2: 
        resource: 
            tokenInfoUri: http://ssoserver:port/oauth/check_token #token的rest api地址
            preferTokenInfo: true
            #userInfoUri: http://ssoserver:port/oauth/userInfo #若使用 userInfo 接口，则除了配置 userInfoUri 属性，还需要配置 preferTokenInfo=false 
        client: 
            clientId: <clientId>
            clientSecret: <clientSecret>
            preEstablishedRedirectUri: http://localhost:9527/oauth2Login #前端oauth2Login登录页面，需要同时配置 useCurrentUri=false 
            useCurrentUri: false
            accessTokenUri: http://ssoserver:port/oauth/token #获取用户accessToken的rest api 地址
            tokenName: token
            userAuthorizationUri: http://ssoserver:port/oauth/authorize #用户授权地址
```
### 3、扩展单点登录用户信息加载接口
```Java
@Service
public class SsoUserDetailService implements SSoUserDetailsService {
    @Override
    public UserDetails loadUserByOAuth2User(Map<String, ?> map) {
        Map<String, ?> userInfo = (Map<String, ?>)map.get("user_info");
        String userId = (String)userInfo.get("user_id");
        UserDetails userDetail = loadUserDetails(userId);
        return userDetail;
    }
}
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

使用spring mvc配置支持跨域
```yaml
jfish: 
    mvc: 
        cors: 
            - mapping: /**
```

使用corsfilter配置支持跨域。
若项目有使用security时，推荐使用此filter配置。
因为CorsFilter 须在所有filter之前，包括security的filter
否则会抛 No 'Access-Control-Allow-Origin' header is present on the requested resource
```yaml
jfish:
    corsfilter: 
        enabled: true
```

### websocket和stomp配置


```yaml
jfish:
	websocket: 
		enabled: true
	    stomp:
			endpoints: /stomp
			allowedOrigins: '*'
	    broker:
			simplePrefixes: /broadcast
			appPrefixes: /app
			userPrefix: /user
```

如果使用了security，且需要跨域时，除了启用相关的跨域配置外，还需要把/stomp/**添加到忽略列表
jfish:
    security:
        ignoringUrls:
            - /static/js/**
            - /stomp/**
```

### activemq之mqtt支持
增加下面配置即可启用mqtt支持
- 1、在pom文件加入activemq的相关依赖
- 2、在application.yaml里增加下面的配置：
```yaml
jfish:
    activemq: 
        mqtt: 
            server-urls: tcp://activemqhost:1883
            username: user
            password: password
            cleanSession: false
```

#### 配置消息outbound
相当于消息发布者
在application.yaml里增加下面的配置：
```yaml
jfish:
    activemq: 
        mqtt: 
            outbound: 
                clientId: producerId
                defaultQos: 1
                converter: json
```

#### 配置消息inbound
有两种方式
- 直接在application.yaml里增加下面的配置：
```yaml
jfish:
    activemq: 
        mqtt: 
            inbounds: 
                - clientId: consumerId
                  topics: SewageStation.device.test.post
                  channelName: SewageStationDeviceInBoundChannel
                  converter: json
```

- 通过注解方式
有时候你可能需要根据类型注入 MqttPahoMessageDrivenChannel，
这时候可以通过实现MqttPahoMessageDrivenChannel的子类，并通过注解标注

```Java
@MqttInboundHandler(
		clientId="clientId", 
		channelName="channelName",
		converter=MessageConverters.JSON,
		topics="test"
)
public class TestInBoundChannelAdapter extends MqttPahoMessageDrivenChannel {
	public TestInBoundChannelAdapter(InBoundClientProps clientConfig, MqttPahoClientFactory clientFactory) {
		super(clientConfig, clientFactory);
	}
}
```
这样你就可以在任务地方通过服务注入的方式得到 TestInBoundChannelAdapter 了。

使用此功能需要在配置类增加 @EnableMqttInbound 注解
```Java
@SpringBootApplication
@EnableMqttInbound
public class TestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestServiceApplication.class, args);
    }
}
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