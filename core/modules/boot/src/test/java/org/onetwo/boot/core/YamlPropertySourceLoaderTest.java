package org.onetwo.boot.core;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlPropertySourceLoaderTest {

	@Test
	public void test() throws Exception {
		YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
		List<PropertySource<?>> props = loader.load("application",
				SpringUtils.newClassPathResource("application.yaml"));
		Object env = props.get(0).getProperty("spring.profiles.active");
		System.out.println("env: " + env);
		env = props.get(0).getProperty("server.port");
		System.out.println("port: " + env);

	}

	@Test
	public void convertCamelToKebab() {
		String path = "D:\\mydev\\java\\workspace\\bitbucket\\onetwo-web-manager\\src\\main\\resources\\application-dev.yaml";
		InputStream in = FileUtils.newInputStream(path);
		Yaml yaml = new Yaml();
		Map<String, Object> map = yaml.load(in);
		System.out.println("map: " + map);

		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		StringWriter buf = new StringWriter();
		Yaml dump = new Yaml(options);
		dump.dump(map, buf);
		System.out.println("res: " + buf);
	}
}
