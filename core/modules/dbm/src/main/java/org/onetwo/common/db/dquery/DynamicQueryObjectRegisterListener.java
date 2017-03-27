package org.onetwo.common.db.dquery;

import org.onetwo.dbm.spring.DbmEntityManagerCreateEvent;
import org.springframework.context.ApplicationListener;

public class DynamicQueryObjectRegisterListener implements ApplicationListener<DbmEntityManagerCreateEvent> {

	@Override
	public void onApplicationEvent(DbmEntityManagerCreateEvent event) {
//		DbmDaoCreateEvent dbevent = (DbmDaoCreateEvent) event;
		FileScanBasicDynamicQueryObjectRegister register = new FileScanBasicDynamicQueryObjectRegister(event.getRegistry());
//		register.setDatabase(dbevent.getDbmSessionFactory().getDialect().getDbmeta().getDataBase());
		register.registerQueryBeans();
	}

}
