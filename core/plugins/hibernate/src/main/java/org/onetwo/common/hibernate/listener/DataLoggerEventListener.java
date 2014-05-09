package org.onetwo.common.hibernate.listener;

import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.type.Type;
import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.log.DataChangedContext;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SuppressWarnings("serial")
public class DataLoggerEventListener implements PreInsertEventListener, PreUpdateEventListener, PreDeleteEventListener, ApplicationContextAware {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());

    private ContextHolder contextHolder;
    
    protected DataChangedContext getDataLogContext(){
		if(contextHolder==null)
			return null;
		return contextHolder.getDataChangedContext();
	}

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		DataChangedContext dc = getDataLogContext();
		try {
			if(dc!=null)
				dc.updateData(event.getEntity(), event.getId(), event.getState(), event.getOldState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());
		} catch (Exception e) {
			logger.error("access logger [updateData] error : " + e.getMessage());
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
		DataChangedContext dc = getDataLogContext();
		try {
			if(dc!=null)
				dc.saveData(event.getEntity(), event.getId(), event.getState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());
		} catch (Exception e) {
			logger.error("access logger [saveData] error : " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean onPreDelete(PreDeleteEvent event) {
		DataChangedContext dc = getDataLogContext();
		try {
			if(dc!=null)
				dc.deleteData(event.getEntity(), event.getId(), event.getDeletedState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());
		} catch (Exception e) {
			logger.error("access logger [deleteData] error : " + e.getMessage());
		}
		return false;
	}
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.contextHolder = SpringUtils.getBean(applicationContext, ContextHolder.class);
	}

}
