package org.onetwo.common.db.dquery;

import org.onetwo.common.jfishdbm.spring.DbmDaoCreateEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class DynamicQueryObjectRegisterListener implements ApplicationListener {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof DbmDaoCreateEvent){
			DbmDaoCreateEvent dbevent = (DbmDaoCreateEvent) event;
			DynamicQueryObjectRegister register = new DynamicQueryObjectRegister(dbevent.getRegistry());
			register.setDatabaseName(dbevent.getDaoImplementor().getDialect().getDbmeta().getDbName());
			register.registerQueryBeans();
		}
		
	}

}
