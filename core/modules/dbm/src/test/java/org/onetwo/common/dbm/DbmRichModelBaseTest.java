package org.onetwo.common.dbm;



import javax.annotation.Resource;
import javax.sql.DataSource;

import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.config.JFishProfile;
import org.onetwo.common.spring.test.SpringBaseJUnitTestCase;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.spring.EnableDbm;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ActiveProfiles({ "dev" })
//@ContextConfiguration(value="classpath:/applicationContext-test.xml")
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
//@Rollback(false)
public class DbmRichModelBaseTest extends SpringBaseJUnitTestCase {
	
	@Configuration
	@JFishProfile
	@ImportResource("classpath:conf/applicationContext-test.xml")
	@EnableDbm
	@ComponentScan(basePackageClasses=DbmRichModelBaseTest.class)
	public static class DbmOrmTestInnerContextConfig {

		@Resource
		private DataSource dataSource;
		@Bean
		public CacheManager cacheManager() {
			JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
			return cache;
		}
		@Bean
		public DbmConfig customDbmConfig(){
			DefaultDbmConfig config = new DefaultDbmConfig();
			config.setModelPackagesToScan("org.onetwo.common.dbm.richmodel");
			return config;
		}
		
	}
}
