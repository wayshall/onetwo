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
    <version>4.5.0-SNAPSHOT</version>
</dependency>

```
spring的依赖请自行添加。

## 启用
在spring配置类（即有@Configuration注解的类）上加上注解@EnableRocketMQ 。
```java     
  
	@EnableRocketMQ
	@Configuration
	public class SpringContextConfig {
	}   
   
```
然后在spring的属性配置文件里加上nameserver的配置：
rocketmq.namesrvAddr=127.0.0.1:9876

## 消费组使用
使用@RMQConsumer注解标注作为consumer的bean，然后使用@RMQSubscribe标注消费者的方法，注意，该方法有且只有一个MessageExt类型参数。
```java
@RMQConsumer
public class ProductTestAppMQConsumer{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RMQSubscribe(groupName="search-consumer-product-test", topic="product")
	public void doConsume(MessageExt msg) {
		logger.info("receive id: {}, topic: {}, tag: {}", msg.getMsgId(),  msg.getTopic(), msg.getTags());
	}

}
```
启动spring容器后将会自动启动消费组。
