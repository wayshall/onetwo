package org.onetwo.common.spring.config;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.propconf.Environment;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@ImportResource({"classpath:webconf/applicationContext-base.xml"})
public class JFishProfiles {

        @Configuration
        @Profile(Environment.PRODUCT)
        static class ProductConcfig {

                @Bean
                public static PropertyPlaceholderConfigurer productConf() {
                        return SpringUtils.newApplicationConf("webconf/application.properties", "webconf/application-product.properties");
                }

        }

        @Configuration
        @Profile(Environment.TEST)
        static class TestConcfig {

                @Bean
                public static PropertyPlaceholderConfigurer testConf() {
                        return SpringUtils.newApplicationConf("webconf/application.properties", "webconf/application-test.properties");
                }
/*
                @Bean
                public AnnotationMethodHandlerAdapter handlerAdapter(){
                        AnnotationMethodHandlerAdapter ha = new AnnotationMethodHandlerAdapter();
                        return ha;
                }*/
        }

        @Configuration
        @Profile(Environment.TEST_LOCAL)
        static class TestLocalConcfig {

                @Bean
                public static PropertyPlaceholderConfigurer testConf() {
                        return SpringUtils.newApplicationConf("webconf/application.properties", "webconf/application-test-local.properties");
                }

        }

        @Configuration
        @Profile(Environment.DEV)
        static class DevConcfig {

                @Bean
                public static PropertyPlaceholderConfigurer devConf() {
                        PropertyPlaceholderConfigurer dev = SpringUtils.newApplicationConf("webconf/application.properties", "webconf/application-dev.properties");
                        return dev;
                }

        }

        @Configuration
        @Profile(Environment.DEV_LOCAL)
        static class DevLocalConcfig {

                @Bean
                public static PropertyPlaceholderConfigurer devConf() {
                        return SpringUtils.newApplicationConf("webconf/application.properties", "webconf/application-dev-local.properties");
                }

        }
}