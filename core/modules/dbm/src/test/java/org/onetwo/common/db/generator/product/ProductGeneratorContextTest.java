package org.onetwo.common.db.generator.product;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductGeneratorContextTest {

    @Bean
    public PropertyPlaceholderConfigurer applicationConfig() {
    	return SpringUtils.newApplicationConf("/db/generator/product/product-jdbc.properties");
    }
}
