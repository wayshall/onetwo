package org.onetwo.common.hibernate.listener;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.hibernate.tuple.StandardProperty;
import org.onetwo.common.db.IBaseEntity;

@SuppressWarnings("serial")
public class TimestampEventListener implements PreInsertEventListener, SaveOrUpdateEventListener {
	
	

	/*@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		StandardProperty[] props = event.getPersister().getEntityMetamodel().getProperties();
		Object[] currentState = event.getState();
		
		if (event.getEntity() instanceof IBaseEntity) {
			for (int i = 0; i < props.length; i++) {
				if ("lastUpdateTime".equals(props[i].getName())) {
					currentState[i] = new Date();
					return false;
				}
			}
		}
		return false;
	}*/

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		final Object object = event.getObject();
		if (object instanceof IBaseEntity) {
			IBaseEntity entity = (IBaseEntity) object;
			entity.setLastUpdateTime(new Date());
		}
	}

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		StandardProperty[] props = event.getPersister().getEntityMetamodel().getProperties();
		Object[] currentState = event.getState();
		
		if (event.getEntity() instanceof IBaseEntity) {
			for (int i = 0; i < props.length; i++) {
				if ("createTime".equals(props[i].getName())) {
					currentState[i] = new Date();
					return false;
				}
			}
		}
		return false;
	}

}
