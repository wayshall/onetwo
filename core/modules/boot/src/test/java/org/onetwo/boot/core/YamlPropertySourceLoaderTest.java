package org.onetwo.boot.core;


import java.util.List;

import org.junit.Test;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;

public class YamlPropertySourceLoaderTest {

	@Test
	public void test() throws Exception{
		YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
		List<PropertySource<?>> props = loader.load("application", SpringUtils.newClassPathResource("application.yaml"));
		Object env = props.get(0).getProperty("spring.profiles.active");
		System.out.println("env: " + env);
		env = props.get(0).getProperty("server.port");
		System.out.println("port: " + env);
	}
}
