package org.onetwo.common.db.dquery;

import org.onetwo.common.jfishdbm.spring.DbmDaoCreateEvent;
import org.springframework.context.ApplicationListener;

public class DynamicQueryObjectRegisterListener implements ApplicationListener<DbmDaoCreateEvent> {

	@Override
	public void onApplicationEvent(DbmDaoCreateEvent event) {
		DbmDaoCreateEvent dbevent = (DbmDaoCreateEvent) event;
		DynamicQueryObjectRegister register = new DynamicQueryObjectRegister(event.getRegistry());
		register.setDatabase(dbevent.getDaoImplementor().getDialect().getDbmeta().getDataBase());
		register.registerQueryBeans();
		
	}

}
