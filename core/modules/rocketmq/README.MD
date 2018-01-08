# rocketmq
spring rocketmq的集成模块

## maven
当前snapshot版本：4.5.0-SNAPSHOT

若使用snapshot版本，请添加snapshotRepository仓储：
```xml
<repository>
     <id>oss</id>
     <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>   
```

添加依赖：   
```xml

<dependency>
    <groupId>org.onetwo4j</groupId>
    <artifactId>onetwo-rocketmq</artifactId>
    <version>4.6.1-SNAPSHOT</version>
</dependency>

```
spring的依赖请自行添加。

## 启用
在spring配置类（即有@Configuration注解的类）上加上注解@EnableONSClient 。
```java     
  
	@EnableONSClient
	@Configuration
	public class SpringContextConfig {
	}   
   
```
这个注解会自动查找标注了@ONSConsumer注解的bean，并注册为ons的消费者，详见下文。

如果项目有生产者，则可以增加producers配置
```java     
  
	@EnableONSClient(producers=@ONSProducer(producerId="producerId"))
	@Configuration
	public class SpringContextConfig {
	}   
```
producerId为你在阿里云mq服务申请的producerId。
如果服务器是开源版的rocketmq，则producerId为生产组名称。

## ons配置
客户端默认服务器为阿里云ons，一般情况下，你需要提供下面三个配置：
```yaml
jfish.ons.accessKey=你的阿里云服务的accessKey
jfish.ons.secretKey=你的阿里云服务的secretKey
jfish.ons.onsAddr=http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet
```

## rocketmq配置
如果mq服务器为开源版的rocketmq，你需要填写一下配置：

```yaml
jfish.ons.serverType=rocketmq #配置mq服务器为rocketmq
jfish.ons.onsAddr=broker地址
jfish.ons.commons.NAMESRV_ADDR=nameserver的地址
```

## 消费组使用
使用@RMQConsumer注解标注作为consumer的bean，然后使用@RMQSubscribe标注消费者的方法，注意，该方法有且只有一个MessageExt类型参数。
```java
@ONSConsumer
public class ProductTestAppMQConsumer{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ONSSubscribe(consumerId="search-consumer-product-test", topic="product")
	public void doConsume(ConsumContext consumContext, String body) {
		System.out.println("注解消费者，收到消息：" + body);
	}

}
```
启动spring容器后将会自动启动消费组。
