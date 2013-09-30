package org.onetwo.common.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class ExtLocalSessionFactoryBean extends LocalSessionFactoryBean implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	private PreInsertEventListener[] preInsertEventListeners;
	private PreUpdateEventListener[] preUpdateEventListeners;
	private SaveOrUpdateEventListener[] saveOrUpdateEventListeners;
	
	public ExtLocalSessionFactoryBean(){
	}
	

	protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder sfb) {
		/*if(sfb.getInterceptor()==null){
			sfb.setInterceptor(new TimestampInterceptor());
		}*/
		sfb.setNamingStrategy(new ImprovedNamingStrategy());
		
		SessionFactory sf = super.buildSessionFactory(sfb);
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) sf;
		EventListenerRegistry reg = sfi.getServiceRegistry().getService(EventListenerRegistry.class);
		
		if(preInsertEventListeners==null){
			List<PreInsertEventListener> preInserts = SpringUtils.getBeans(applicationContext, PreInsertEventListener.class);
			this.preInsertEventListeners = preInserts.toArray(new PreInsertEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.PRE_INSERT).appendListeners(preInsertEventListeners);

		if(preUpdateEventListeners==null){
			List<PreUpdateEventListener> preUpdates = SpringUtils.getBeans(applicationContext, PreUpdateEventListener.class);
			this.preUpdateEventListeners = preUpdates.toArray(new PreUpdateEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.PRE_UPDATE).appendListeners(preUpdateEventListeners);

		if(saveOrUpdateEventListeners==null){
			List<SaveOrUpdateEventListener> preUpdates = SpringUtils.getBeans(applicationContext, SaveOrUpdateEventListener.class);
			this.saveOrUpdateEventListeners = preUpdates.toArray(new SaveOrUpdateEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.SAVE_UPDATE).appendListeners(saveOrUpdateEventListeners);
//		reg.getEventListenerGroup(EventType.SAVE_UPDATE).appendListener(new SaveOrUpdateTimeListener());
		HibernateUtils.initSessionFactory(sf);
		return sf;
	}

	public void setPreInsertEventListeners(PreInsertEventListener[] preInsertEventListeners) {
		this.preInsertEventListeners = preInsertEventListeners;
	}

	public void setPreUpdateEventListeners(PreUpdateEventListener[] preUpdateEventListeners) {
		this.preUpdateEventListeners = preUpdateEventListeners;
	}

	public void setSaveOrUpdateEventListeners(SaveOrUpdateEventListener[] saveOrUpdateEventListeners) {
		this.saveOrUpdateEventListeners = saveOrUpdateEventListeners;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
