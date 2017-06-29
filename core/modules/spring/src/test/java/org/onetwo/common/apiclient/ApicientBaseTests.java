package org.onetwo.common.apiclient;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.apiclient.ApicientBaseTests.ApiclientBaseTestInnerContextConfig;
import org.onetwo.common.apiclient.annotation.EnableRestApiClient;
import org.onetwo.common.propconf.Environment;
import org.onetwo.common.spring.config.JFishProfile;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes=ApiclientBaseTestInnerContextConfig.class)
@ActiveProfiles(Environment.TEST)
public class ApicientBaseTests {
	
	@BeforeClass
	public static void setupClass(){
	}
	@Test
	public void contextLoads() {
	}
	
	@Configuration
	@JFishProfile
	@EnableRestApiClient(baseUrl="http://www.weather.com.cn/data")
	public static class ApiclientBaseTestInnerContextConfig {
	}

}
