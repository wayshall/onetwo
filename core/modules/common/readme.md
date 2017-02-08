#common 
交流群：  8060215    
一些通用功能，使用频繁的工具类的封装

##maven
```xml

<dependency>
    <groupId>org.onetwo4j</groupId>
    <artifactId>onetwo-common</artifactId>
    <version>4.3.9-SNAPSHOT</version>
</dependency>

```
##常用工具类
###LangUtils 一些常用的工具方法集合
###ReflectUtils 反射相关工具类
###DateUtils 日期相关工具类
###FileUtils 文件相关工具类
###Types 简单的值转换
```java   
Long val = Types.convertValue("1", Long.class);  
```
###MoneyConvertUtils 金额转为大写中文
```java   
String result = MoneyConvertUtils.convert(3.3)   
Integer[] values = Types.asArray("1,2,3", Integer.class)
```
...and so on

