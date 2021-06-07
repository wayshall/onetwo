package org.onetwo.common.apiclient;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.apiclient.ApiClientBaseTests.ApiclientBaseTestInnerContextConfig;
import org.onetwo.common.apiclient.ApiClientBaseTests.YsApiclientBaseTestInnerContextConfig;
import org.onetwo.common.apiclient.annotation.EnableRestApiClient;
import org.onetwo.common.apiclient.api.simple.WeatherClient;
import org.onetwo.common.apiclient.api.simple2.EnableSimpleApiClient;
import org.onetwo.common.apiclient.api.simple2.Ys7AccessTokenClient;
import org.onetwo.common.apiclient.interceptor.RestExecutorSimpleLogInterceptor;
import org.onetwo.common.propconf.Environment;
import org.onetwo.common.spring.config.JFishProfile;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes= {ApiclientBaseTestInnerContextConfig.class, YsApiclientBaseTestInnerContextConfig.class})
@ActiveProfiles(Environment.TEST)
public class ApiClientBaseTests {
	
	@BeforeClass
	public static void setupClass(){
	}
	@Test
	public void contextLoads() {
	}
	
	@Configuration
	@JFishProfile
	@EnableRestApiClient(baseUrl=WeatherClient.BASE_URL, basePackageClasses= {WeatherClient.class})
	@ComponentScan(basePackageClasses=RestExecutorSimpleLogInterceptor.class)
	public static class ApiclientBaseTestInnerContextConfig {
		
		/*@Bean
		public ClientHttpRequestInterceptor restExecutorSimpleLogInterceptor(){
			return new RestExecutorSimpleLogInterceptor();
		}*/
	}
	
	@Configuration
	@JFishProfile
	@EnableSimpleApiClient(baseUrl=Ys7AccessTokenClient.BASE_URL, basePackageClasses=Ys7AccessTokenClient.class)
	// 错误，下面的注解会使用ApiclientBaseTestInnerContextConfig类配置了的BASE_URL
//	@EnableRestApiClient(baseUrl=WeatherClient.BASE_URL, basePackageClasses=Ys7AccessTokenClient.class)
	@ComponentScan(basePackageClasses=RestExecutorSimpleLogInterceptor.class)
	public static class YsApiclientBaseTestInnerContextConfig {
		
		/*@Bean
		public ClientHttpRequestInterceptor restExecutorSimpleLogInterceptor(){
			return new RestExecutorSimpleLogInterceptor();
		}*/
	}

}
