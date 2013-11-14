package org.onetwo.common.web.config;

import org.onetwo.common.spring.config.JFishProfiles;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:applicationContext.xml" })
@Import(JFishProfiles.class)
public class ClassPathApplicationContext extends BaseApplicationContextSupport {

}
