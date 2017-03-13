package org.onetwo.dbm.spring;

import org.onetwo.dbm.support.DbmSessionFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DbmDaoCreateEvent extends ApplicationEvent {

	private BeanDefinitionRegistry registry;
	
	public DbmDaoCreateEvent(Object source, BeanDefinitionRegistry registry) {
		super(source);
		this.registry = registry;
	}

	public DbmSessionFactory getDbmSessionFactory(){
		return (DbmSessionFactory)super.getSource();
	}

	public BeanDefinitionRegistry getRegistry() {
		return registry;
	}

}
