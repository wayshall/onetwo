package org.onetwo.plugin.hibernate;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.hibernate.HibernateEntityManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=HibernatePlugin.class)
public class HibernatePluginContext {
	
	@Bean
	public BaseEntityManager baseEntityManager(){
		return new HibernateEntityManagerImpl();
	}
	
}
