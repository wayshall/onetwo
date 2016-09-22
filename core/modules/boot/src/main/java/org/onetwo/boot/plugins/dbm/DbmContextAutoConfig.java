package org.onetwo.boot.plugins.dbm;

import org.onetwo.common.db.dquery.DynamicQueryObjectRegisterListener;
import org.onetwo.dbm.spring.JFishdbmSpringConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(JFishdbmSpringConfiguration.class)
@Import({JFishdbmSpringConfiguration.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class DbmContextAutoConfig {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public DbmContextAutoConfig(){
	}
	
	@Bean
	public DynamicQueryObjectRegisterListener dynamicQueryObjectRegisterListener(){
		return new DynamicQueryObjectRegisterListener();
	}
	
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
