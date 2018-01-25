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
jfish: 
    ons: 
        accessKey: 你的阿里云服务的accessKey
        secretKey: 你的阿里云服务的secretKey
        onsAddr: http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet
```

## rocketmq配置
如果mq服务器为开源版的rocketmq，你需要填写一下配置：

```yaml
jfish: 
    ons: 
        serverType: rocketmq #配置mq服务器为rocketmq
        namesrvAddr: nameserver的地址
        onsAddr: broker地址
```

## 生产者使用
使用注解@EnableONSClient 启用rocketmq模块后，直接注入org.onetwo.ext.ons.producer.ProducerService服务发送消息即可：
```Java

@Transactional
public class DataBaseProducerServiceImpl {

	@Autowired
	ProducerService onsProducerService;

	public void sendMessage(){
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
																	  .topic(RmqONSProducerTest.TOPIC)
																	  .tags(RmqONSProducerTest.ORDER_PAY)
																	  .body(OrderTestMessage.builder()
																			  				.orderId(1L)
																			  				.title("支付")
																			  				.build())
																	  .build());
		System.out.println("res: " + res);
		
		
	}
}
```

## 消费者使用
使用@ONSConsumer 注解标注作为consumer的bean，然后使用@ONSSubscribe 标注消费者的方法，消费方法只有一个ConsumContext类型的参数
```java
@ONSConsumer
public class ProductTestAppMQConsumer{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ONSSubscribe(consumerId="search-consumer-product-test", topic="product")
	public void doConsume(ConsumContext consumContext) {
		System.out.println("注解消费者，收到消息：" + consumContext.getDeserializedBody());
	}

}
```
如果不使用@ONSSubscribe 注解方法，则可以实现CustomONSConsumer接口。   


启动spring容器后将会自动启动消费组。


## 事务消息
### 阿里云事务消息
如果是使用阿里云上的消息服务，可以直接在阿里云上申请一个事务消息的producerId，然后按下面方式启用配置：
```java     
@EnableONSClient(producers=@ONSProducer(producerId="producerId", transactional=true))
@Configuration
public class SpringContextConfig {
}   
```
启用后，直接注入TransactionProducerService服务即可使用ons的原生事务消息。
```Java

@Transactional
public class DataBaseProducerServiceImpl {

	@Autowired
	TransactionProducerService transactionProducerService;

	public void sendMessage(){
		//详细参见阿里云事务消息的使用方式
		transactionProducerService.sendMessage(msg, new LocalTransactionExecuter(){
			public TransactionStatus execute(final Message msg, final Object arg){
				return TransactionStatus.CommitTransaction;
			}
		}, null)
		
	}
}
```
### JFish的rocketmq模块提供的事务消息
JFish的rocketmq利用本地数据库的方式实现了事务消息，不过只有使用框架提供的ProducerService服务接口发送的消息才会被拦截处理为本地事务消息。
启用事务消息配置：
- 1、添加dbm依赖，因为内置使用了dbm来做持久化，你可以通过自定义SendMessageRepository接口来避免dbm依赖：   
```xml

<dependency>
    <groupId>org.onetwo4j</groupId>
    <artifactId>onetwo-dbm</artifactId>
    <version>4.6.1-SNAPSHOT</version>
</dependency>

```
dbm 详情见：[dbm](https://github.com/wayshall/dbm)
启用dbm需要在配置类增加@EnableDbm 注解：
```Java
@EnableDbm
@Configuration
public class SpringContextConfig {
}   
```

dbm作为一个orm，自然需要配置数据源，请自行配置。

- 2、在本地数据库增加保存发送消息的表：
```sql
drop table if exists data_rmq_send;

/*==============================================================*/
/* Table: data_rmq_send                                         */
/*==============================================================*/
create table data_rmq_send
(
   msgkey               varchar(64) not null,
   body                 blob,
   state                tinyint comment '消息状态：
            待发送：0
            已发送：1
            正常来说只有待发送，因为发送后一般不保留，直接删掉',
   create_at            datetime,
   update_at            datetime,
   primary key (msgkey)
);

alter table data_rmq_send comment 'rmq消息发送暂存表';
```

- 3、增加使用事务消息的配置
```yaml
jfish: 
    ons: 
        transactional: 
            enabled: true
```

完成上面三步后，注入并通过ProducerService接口的sendMessage(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate)方法发送的消息，都会首先保存到本地数据库，待事务提交后自动把消息发送出去，并删除数据表的rocketmq消息。代用代码如下：

```Java

@Transactional
public class DataBaseProducerServiceImpl {

	@Autowired
	ProducerService onsProducerService;

	public void sendMessage(){
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
																	  .topic(RmqONSProducerTest.TOPIC)
																	  .tags(RmqONSProducerTest.ORDER_PAY)
																	  .body(OrderTestMessage.builder()
																			  				.orderId(1L)
																			  				.title("支付")
																			  				.build())
																	  .build(), SendMessageFlags.EnableDatabaseTransactional);
		System.out.println("res: " + res);
		
		
	}
}
```

如果发送或者删除失败，会有定时任务定时扫描消息表并重新发送。
默认配置为一分钟扫描一次，可自定义配置任务的表达式：
```yaml
jfish: 
    ons: 
        transactional: 
            task: 
                cron: 0 0/2 * * * *
```
cron表达式参见spring task文档