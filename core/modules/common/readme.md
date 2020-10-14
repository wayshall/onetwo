# common 
交流群：  604158262    
一些通用功能，使用频繁的工具类的封装

## 要求
JDK 1.8+

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
    <artifactId>onetwo-common</artifactId>
    <version>4.6.0-SNAPSHOT</version>
</dependency>

```
## 常用工具类
### LangUtils 一些常用的工具方法集合

### ReflectUtils 反射相关工具类
   
```java   
//根据属性交集，复制对象
ReflectUtils.copy(source, target);

//根据配置，复制对象
ReflectUtils.copy(source, target, CopyConfig.create().throwIfError(true));

//忽略值为null或者blank的属性
ReflectUtils.copyIgnoreBlank(source, target);

//把对象转为map，对象的属性名称为key，对应属性的值为value
Map<String, Object> map = ReflectUtils.toMap(user);

//更多定制转换规则
BeanToMapBuilder.newBuilder()
				.enableUnderLineStyle()
				.build()
				.toMap(user)

//实例化一个UserEntity对象，并把map的值设置到UserEntity对象
UserEntity user = ReflectUtils.fromMap(map, UserEntity.class);
```


### bean转成map
把一个pojo转换为map，其中pojo的属性名作为key，属性值作为对象
```java   

//把对象转为map，对象的属性名称为key，对应属性的值为value
Map<String, Object> map = ReflectUtils.toMap(user);

//更多定制转换规则
BeanToMapBuilder.newBuilder()
				.propertyAcceptor((p, v)->v!=null) //只有属性值不为null的才转为map
				.enableUnderLineStyle() //把属性的驼峰命名风格转为下划线风格
				.build()
				.toMap(user)
```

### DateUtils 日期相关工具类
### FileUtils 文件相关工具类
### Types 简单的值转换
```java   
Long val = Types.convertValue("1", Long.class);  

// 枚举转换   
public enum EnumValues {
    NORMAL,
    DELETE;
}
EnumValues enumValue = Types.convertValue("NORMAL", EnumValues.class);
        
实现 EnumerableTextLabel 接口，可实现通过定义text转换
public enum EnumValues implements EnumerableTextLabel {
    NORMAL("正常"),
    DELETE("已删除");
    final private String label;
    private EnumerableTextLabelValues(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}

EnumValues enumValue = Types.convertValue("正常", EnumValues.class);

    
```
### MoneyConvertUtils 金额转为大写中文
```java   
String result = MoneyConvertUtils.convert(3.3)   
Integer[] values = Types.asArray("1,2,3", Integer.class)
```
...and so on

