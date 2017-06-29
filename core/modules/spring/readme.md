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

## rpc接口风格的 rest client

### 注解启用
在spring配置类（即有@Configuration注解的类）上加上注解@EnableRestApiClient 即可。
```java     
  
	@EnableRestApiClient
	public class SpringContextConfig {
	}   
   
```


### 创建自定义的rest client接口

比如创建一个调用 http://www.weather.com.cn/data/sk/101010100.html 的rest client

对应接口代码如下：
```Java

@RestApiClient(url="http://www.weather.com.cn/data")
public interface WeatherClient {
	
	@GetMapping(value="/sk/{cityid}.html")
	WeatherResponse getWeather(@PathVariable("cityid") String cityid);

}

@SupportedMediaType(MediaType.TEXT_HTML_VALUE)
public class WeatherResponse {
	
	private Weatherinfo weatherinfo;

	@Data
	public static class Weatherinfo {
		String city;
		String cityid;
		String temp;
		@JsonProperty("WD")
		String WD;
		@JsonProperty("WS")
		String WS;
		@JsonProperty("SD")
		String SD;
		@JsonProperty("WSE")
		String WSE;
		String time;
		String isRadar;
		@JsonProperty("Radar")
		String radar;
		String njd;
		String qy;
		String rain;
	}

}
```   

解释：   
- 所有rest客户端接口必须使用@RestApiClient 注解标注为rest接口
- @RestApiClient 注解的path指定本接口下的所有方法的请求路径都是“/sk/{cityid}.html”的子路径
- @GetMapping 是spring mvc的注解，本库利用了一些spring mvc的现成注解，@GetMapping(value="/create")标识这个方法是 get 请求，并且请求路径是/sk/{cityid}.html
- @PathVariable 表示 getWeather 参数将会用于解释请求路径
- WeatherResponse 是一个VO，表示响应内容，按照返回的格式编写即可。
- @SupportedMediaType 注解是为了兼容一些不规范的接口，比如这个天气接口，明明返回的是json格式数据，但response的contentType却是text/html。

至此，一个rest客户端接口就编写完成了。

### 使用接口
使用的时候，只需要直接把接口用spring的方式直接注入示例即可使用，如：
```Java

public class MenuServiceTest {
	
	@Autowired
	MenuService menuService;
	
	@Test
	public void testCreateMenu() throws IOException{
		String classPath = "menu_create.json";

		CreateMenuRequest request = ....;
		WechatResponse res = menuService.create(request);
		assertThat(res.isSuccess()).isTrue();
	}

}
```

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