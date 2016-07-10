package org.onetwo.common.db.generator.emall;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmallGeneratorContextTest {

    @Bean
    public PropertyPlaceholderConfigurer applicationConfig() {
    	return SpringUtils.newApplicationConf("/db/generator/emall/emall-jdbc.properties");
    }
}
