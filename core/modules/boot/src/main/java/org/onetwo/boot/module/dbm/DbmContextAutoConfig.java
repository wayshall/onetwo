package org.onetwo.boot.module.dbm;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.db.dquery.DynamicQueryObjectRegisterListener;
import org.onetwo.dbm.spring.DbmSpringConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(DbmSpringConfiguration.class)
@Import({DbmSpringConfiguration.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(BootDbmConfig.class)
public class DbmContextAutoConfig {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private BootJFishConfig bootJFishConfig;
	
	public DbmContextAutoConfig(){
	}
	
	@Bean
	public DynamicQueryObjectRegisterListener dynamicQueryObjectRegisterListener(){
		return new DynamicQueryObjectRegisterListener();
	}
	
	/*@Bean
	@ConditionalOnMissingBean(DataBaseConfig.class)
	public DataBaseConfig dataBaseConfig(){
		return new BootDataBaseConfig();
	}*/
	/*@Bean
	public DynamicQueryObjectRegisterListener2 dynamicQueryObjectRegisterListener(){
		return new DynamicQueryObjectRegisterListener2();
	}
	
	public class DynamicQueryObjectRegisterListener2 implements ApplicationListener<DataSourceInitializedEvent> {

		@Override
		public void onApplicationEvent(DataSourceInitializedEvent event) {
			DataSource ds = (DataSource) event.getSource();
			DataBase db = JdbcUtils.getDataBase(ds);
			DynamicQueryObjectRegister register = new DynamicQueryObjectRegister(applicationContext);
			register.setDatabase(db);
			register.registerQueryBeans();
			
		}

	}*/

}
