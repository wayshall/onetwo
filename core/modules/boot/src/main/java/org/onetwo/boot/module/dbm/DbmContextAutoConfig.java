package org.onetwo.boot.module.dbm;

import org.onetwo.dbm.spring.DbmSpringConfiguration;
import org.onetwo.dbm.spring.DynamicQueryObjectRegisterConfigration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(DbmSpringConfiguration.class)
@Import({DbmSpringConfiguration.class, DynamicQueryObjectRegisterConfigration.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(BootDbmConfig.class)
//@EnableDbm
public class DbmContextAutoConfig {
	
	private BootDbmConfig bootDbmConfig;
	
	public DbmContextAutoConfig(BootDbmConfig bootDbmConfig){
		this.bootDbmConfig = bootDbmConfig;
	}
	
	/*@Bean
	public DynamicQueryObjectRegisterListener dynamicQueryObjectRegisterListener(){
		return new DynamicQueryObjectRegisterListener();
	}*/
	
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
