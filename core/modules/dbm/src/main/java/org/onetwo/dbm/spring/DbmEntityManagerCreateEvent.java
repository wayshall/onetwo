package org.onetwo.dbm.spring;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.core.spi.DbmEntityManager;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DbmEntityManagerCreateEvent extends ApplicationEvent {
	
	public static void publish(ApplicationContext applicationContext, DbmEntityManager dbmEntityManager){
		BeanDefinitionRegistry registry = SpringUtils.getBeanDefinitionRegistry(applicationContext);
		DbmEntityManagerCreateEvent event = new DbmEntityManagerCreateEvent(dbmEntityManager, registry);
		applicationContext.publishEvent(event);
	}

	private BeanDefinitionRegistry registry;
	
	public DbmEntityManagerCreateEvent(Object source, BeanDefinitionRegistry registry) {
		super(source);
		this.registry = registry;
	}

	public DbmSessionFactory getDbmSessionFactory(){
		return getDbmEntityManager().getSessionFactory();
	}

	public BeanDefinitionRegistry getRegistry() {
		return registry;
	}

	public DbmEntityManager getDbmEntityManager(){
		return (DbmEntityManager) super.getSource();
	}
}
