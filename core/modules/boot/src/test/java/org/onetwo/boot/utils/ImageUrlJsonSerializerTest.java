package org.onetwo.boot.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.ImageServer;
import org.onetwo.boot.utils.ImageUrlJsonSerializerTest.JsonBaseTestInnerContextConfig;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes=JsonBaseTestInnerContextConfig.class)
public class ImageUrlJsonSerializerTest {
	JsonMapper jsonMapper = JsonMapper.ignoreNull();
	@Autowired
	ApplicationContext applicationContext;
	
	@Before
	public void setup() {
		Springs.initApplication(applicationContext);
	}
	
	@Test
	public void testList() {
		JsonImageDataTest image = new JsonImageDataTest();
		image.setImages(Arrays.asList("/aa/bb.jpg", "/cc/dd.gif"));
		String jsonString = jsonMapper.toJson(image);
		System.out.println("json: " + jsonString);
		assertThat(jsonString).contains("http://test.com/images/aa/bb.jpg");
		assertThat(jsonString).contains("http://test.com/images/cc/dd.gif");
		
		JsonSerialDataTest data = new JsonSerialDataTest();
		data.setImageData(image);
		
		Page<JsonSerialDataTest> page = Page.create();
		page.setResult(Arrays.asList(data));
		jsonString = jsonMapper.toJson(page);
		System.out.println("json: " + jsonString);
		assertThat(jsonString).contains("http://test.com/images/aa/bb.jpg");
		assertThat(jsonString).contains("http://test.com/images/cc/dd.gif");
	}

	@Data
	public static class JsonSerialDataTest {
		private JsonImageDataTest imageData;
	}
	@Data
	public static class JsonImageDataTest {
		@JsonSerialize(using=ImageUrlJsonSerializer.class)
		private List<String> images;
	}

	@Configuration
	public static class JsonBaseTestInnerContextConfig {
		
		@Bean
		public BootSiteConfig bootSiteConfig() {
			BootSiteConfig config = new BootSiteConfig();
			ImageServer imageServer = config.new ImageServer();
			imageServer.setBasePath("http://test.com/images");
			config.setImageServer(imageServer);
			return config;
		}
	}
}

