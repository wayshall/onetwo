#spring 
交流群：  604158262    
依赖到spring相关的封装

##maven
```xml

<dependency>
    <groupId>org.onetwo4j</groupId>
    <artifactId>onetwo-spring</artifactId>
    <version>4.6.1-SNAPSHOT</version>
</dependency>

```

## rest client

##工具类
###BeanCopierBuilder 复制JavaBean
```java   
BeanA src = new BeanA();
BeanB target = new BeanB();
BeanCopierBuilder.fromObject(src)
    			.to(target);   

BeanC c = BeanCopierBuilder.fromObject(src)
    						.to(BeanC.class); 
```
如果两个bean之间的属性名称不同，但有特定的转换规则，则可以自定义PropertyNameConvertor，下面示例的属性名称转换器，会把srcBean里的驼峰命名的属性名称转换成下划线风格的属性名称，再复制：
```java   
UnderlineBean target = new UnderlineBean();
BeanCopierBuilder.fromObject(srcBean)
				.propertyNameConvertor(CopyUtils.UNDERLINE_CONVERTOR)
				.to(target);  
```
###BeanToMapBuilder bean转成map
```java
Map<String, Object> map = BeanToMapBuilder.newBuilder().build().toMap(obj)   
//如果obj是个带有“复杂”对象属性的对象，则可以使用toFlatMap递归解释嵌套属性
Map<String, Object> map = BeanToMapBuilder.newBuilder().build().toFlatMap(obj)   
```