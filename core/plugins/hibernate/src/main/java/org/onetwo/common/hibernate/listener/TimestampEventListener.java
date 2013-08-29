package org.onetwo.common.hibernate.listener;

import java.util.Date;

import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.tuple.StandardProperty;
import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.hibernate.HibernateUtils;

@SuppressWarnings("serial")
public class TimestampEventListener implements PreInsertEventListener, PreUpdateEventListener {
	
	

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		StandardProperty[] props = event.getPersister().getEntityMetamodel().getProperties();
		Object[] currentState = event.getState();
		
		if (event.getEntity() instanceof IBaseEntity) {
			HibernateUtils.setPropertyState(props, currentState, "lastUpdateTime", new Date());
		}
		return false;
	}

/*	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		final Object object = event.getObject();
		if (object instanceof IBaseEntity) {
			IBaseEntity entity = (IBaseEntity) object;
			entity.setLastUpdateTime(new Date());
		}
	}*/

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		StandardProperty[] props = event.getPersister().getEntityMetamodel().getProperties();
		Object[] currentState = event.getState();
		
		if (event.getEntity() instanceof IBaseEntity) {
			HibernateUtils.setPropertyState(props, currentState, "createTime", new Date());
		}
		return false;
	}
	

}
