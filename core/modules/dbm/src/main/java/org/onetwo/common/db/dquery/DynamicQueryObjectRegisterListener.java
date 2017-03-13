package org.onetwo.common.db.dquery;

import org.onetwo.dbm.spring.DbmDaoCreateEvent;
import org.springframework.context.ApplicationListener;

public class DynamicQueryObjectRegisterListener implements ApplicationListener<DbmDaoCreateEvent> {

	@Override
	public void onApplicationEvent(DbmDaoCreateEvent event) {
		DbmDaoCreateEvent dbevent = (DbmDaoCreateEvent) event;
		FileScanBasicDynamicQueryObjectRegister register = new FileScanBasicDynamicQueryObjectRegister(event.getRegistry());
		register.setDatabase(dbevent.getDbmSessionFactory().getDialect().getDbmeta().getDataBase());
		register.registerQueryBeans();
	}

}
