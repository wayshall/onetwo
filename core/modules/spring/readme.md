# spring 
交流群：  604158262    
依赖到spring相关的封装

## 目录
- [本地接口风格的 rest client](https://github.com/wayshall/onetwo/tree/master/core/modules/spring#本地接口风格的-rest-client)
- [工具类](#工具类)    
	[复制Bean](#复制Bean)    
	[bean转成map](#bean转成map)


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
    <artifactId>onetwo-spring</artifactId>
    <version>4.7.2-SNAPSHOT</version>
</dependency>

```

## 本地接口风格的 rest client
基于spring RestTemplate上封装的本地接口风格的rest client

### 注解启用
在spring配置类（即有@Configuration注解的类）上加上注解@EnableRestApiClient 即可。
```java     
  
	@EnableRestApiClient
	public class SpringContextConfig {
	}   
   
```


### 创建自定义的rest client接口

比如创建一个获取天气预报（ http://www.weather.com.cn/data/sk/101010100.html ） 的rest client

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

### 自定义header
可直接在client接口里定义参数类型org.springframework.http.HttpHeaders的参数，所有传进去的参数都会直接put到请求头里，或者定义参数类型为org.onetwo.common.apiclient.ApiHeaderCallback的参数，在回调里自行控制。
```Java
@RestApiClient(url="http://www.weather.com.cn/data")
public interface WeatherClient {
	
	@GetMapping(value="/sk/{cityid}.html")
	WeatherResponse getWeather(@PathVariable("cityid") String cityid, HttpHeaders header, ApiHeaderCallback callback);

}
```

### 使用接口
使用的时候，只需要直接把接口用spring的方式直接注入示例即可使用，如：
```Java

public class WeatherClientTest  {
	
	@Autowired
	WeatherClient weatherClient;
	
	@Test
	public void test(){
		WeatherResponse res = this.weatherClient.getWeather("101010100");
		assertThat(res.getWeatherinfo().getCity()).isEqualTo("北京");
	}

}
```
### @ResponseHandler 自定义响应处理器
某些情况下需要自行处理response，可以实现CustomResponseHandler接口，并使用@ResponseHandler注解标记
```Java
class WeatherResponseHandler implements CustomResponseHandler<byte[]> {
	@Override
	public Object handleResponse(ApiClientMethod apiMethod, ResponseEntity<byte[]> responseEntity) {
		return JsonMapper.IGNORE_NULL.fromJson(responseEntity.getBody(), WeatherResponse.class);
	}
}

@RestApiClient(url="http://www.weather.com.cn/data")
public interface WeatherClient {
	
	@GetMapping(value="/sk/{cityid}.html")
	@ResponseHandler(WeatherResponseHandler.class)
	WeatherResponse getWeatherWithHandler(@PathVariable String cityid);

}
```

### 拦截器
RestApiClient 的接口底层实现使用resttemplate，所以若想拦截接口的请求只需要实现spring的ClientHttpRequestInterceptor接口即可，为了和普通的拦截器区分，实现类还必须添加@RestExecutorInterceptor注解。
如简单的打印日志拦截器：
```Java
@RestExecutorInterceptor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExecutorSimpleLogInterceptor implements ClientHttpRequestInterceptor {
	private Logger logger = ApiClientUtils.getApiclientlogger();

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		if(logger.isDebugEnabled()){
			logger.debug("RestExecutor url: {}", request.getURI());
		}
		return execution.execute(request, body);
	}

}
```


## 工具类
### 复制Bean
志在提供一个可定制更灵活的bean复制工具。若
```java   
BeanA src = new BeanA();
BeanB target = new BeanB();
BeanCopierBuilder.fromObject(src).to(target);   

BeanC c = BeanCopierBuilder.fromObject(src).to(BeanC.class); 
```
如果两个bean之间的属性名称不同，但有特定的转换规则，则可以自定义PropertyNameConvertor，下面示例的属性名称转换器，会把srcBean里的驼峰命名的属性名称转换成下划线风格的属性名称，再复制：
```java   
UnderlineBean target = new UnderlineBean();
BeanCopierBuilder.fromObject(srcBean)
		.propertyNameConvertor(CopyUtils.UNDERLINE_CONVERTOR)
		.to(target);  
```
### bean转成map
把一个pojo转换为map，其中pojo的属性名作为key，属性值作为对象
```java
Map<String, Object> map = BeanToMapBuilder.newBuilder().build().toMap(obj)   
//如果obj是个带有“复杂”对象属性的对象，则可以使用toFlatMap递归解释嵌套属性
Map<String, Object> map = BeanToMapBuilder.newBuilder().build().toFlatMap(obj)   
```

## 其他……
待补充。。。