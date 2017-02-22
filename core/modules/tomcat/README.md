#tomcat#
------
简单的内嵌tomcat   
交流群：  8060215

##环境要求
JDK8+

##maven
```xml
       <dependency>
		    <groupId>org.onetwo4j</groupId>
		    <artifactId>onetwo-tomcat</artifactId>
		    <version>4.3.9-SNAPSHOT</version>
		    <scope>test</scope>
		</dependency>
```

##Java代码
创建一个TomcatStarter类，会根据maven的默认目录配置tomcat
```Java

public class TomcatStarter {

	public static void main(String[] args) {
		TomcatServerBuilder.create(8080)
							.build()
							.start();
	}

}


```
