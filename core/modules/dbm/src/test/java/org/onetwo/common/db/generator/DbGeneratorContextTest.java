package org.onetwo.common.db.generator;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbGeneratorContextTest {

    @Bean
    public PropertyPlaceholderConfigurer applicationConfig() {
    	return SpringUtils.newApplicationConf("/db/generator/jdbc.properties");
    }
}
