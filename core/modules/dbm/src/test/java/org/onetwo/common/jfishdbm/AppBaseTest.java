package org.onetwo.common.jfishdbm;



import javax.annotation.Resource;
import javax.sql.DataSource;

import org.onetwo.common.db.dquery.spring.DynamicQueryEnabled;
import org.onetwo.common.jfishdbm.spring.JFishdbmEnabled;
import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.config.JFishProfile;
import org.onetwo.common.spring.test.SpringBaseJUnitTestCase;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ActiveProfiles({ "dev" })
//@ContextConfiguration(value="classpath:/applicationContext-test.xml")
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
@TransactionConfiguration(defaultRollback = false)
public class AppBaseTest extends SpringBaseJUnitTestCase {
	
	@Configuration
	@JFishProfile
	@ImportResource("classpath:conf/applicationContext-test.xml")
	@JFishdbmEnabled
	@DynamicQueryEnabled
	@ComponentScan(basePackageClasses=AppBaseTest.class)
	public static class JFishOrmTestInnerContextConfig {

		@Resource
		private DataSource dataSource;
		@Bean
		public CacheManager cacheManager() {
			JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
			return cache;
		}
		
	}
}
