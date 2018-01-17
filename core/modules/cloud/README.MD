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
    <version>4.6.1-SNAPSHOT</version>
</dependency>

```
## 启动spring cloud扩展
```Java
@EnableJFishCloudExtension
public class Application{
}
```


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