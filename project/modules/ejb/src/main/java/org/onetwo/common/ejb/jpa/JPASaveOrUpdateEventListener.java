package org.onetwo.common.ejb.jpa;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.db.IdEntity;
import org.onetwo.common.db.event.DefaultEventListener;
import org.onetwo.common.db.event.SaveOrUpdateEvent;
import org.onetwo.common.db.event.SaveOrUpdateEventListener;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ReflectUtils;

public class JPASaveOrUpdateEventListener extends DefaultEventListener implements SaveOrUpdateEventListener {

	/*public JPASaveOrUpdateEventListener(AnnotationProcessorManager annotationProcessorManager) {
		super(annotationProcessorManager);
	}*/

	@Override
	protected void init() {
		super.init();
//		registerProcessor(EntityCascade.class, EntityCascadeProcessor.create(EntityAction.SaveOrUpdate));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) {
		Object entity = event.getObject();
		EntityManager em = event.getEventSource().getEntityManager();
		
//		Date now = new Date();
		if(entity instanceof IdEntity){
			IdEntity idEntity = (IdEntity) entity;
			if(idEntity.getId()==null){
//				timeOnCreate(idEntity, now);
				em.persist(entity);
			}else{
				this.checkEntityIdValid(idEntity.getId());
//				timeOnUpdate(idEntity, now);
				entity = em.merge(entity);
				event.setObject(entity);
			}
		}else{
			PropertyDescriptor pd = AnnotationUtils.findAnnotationProperty(entity.getClass(), Id.class);
			if(pd!=null){
				Object idVal = ReflectUtils.getProperty(entity, pd);
				if(idVal==null){
//					timeOnCreate(entity, now);
					em.persist(entity);
				}else{
//					timeOnUpdate(entity, now);
					em.merge(entity);
				}
			}
		}
		
		/*JPAAnnoContext context = new JPAAnnoContext(entity, event.getAction());
		context.setEvent(event);
		autoProcess(context);*/
		
	}
	
	protected void checkEntityIdValid(Serializable id){
		if(!MyUtils.checkIdValid(id))
			throw new ServiceException("invalid id : " + id);
	}

	@SuppressWarnings("rawtypes")
	public void timeOnCreate(Object entity, Date time){
		if(!(entity instanceof IBaseEntity))
			return ;
		IBaseEntity baseEntity = (IBaseEntity) entity;
		baseEntity.setCreateTime(time);
		baseEntity.setLastUpdateTime(time);
	}
	
	@SuppressWarnings("rawtypes")
	public void timeOnUpdate(Object entity, Date time){
		if(!(entity instanceof IBaseEntity))
			return ;
		IBaseEntity baseEntity = (IBaseEntity) entity;
		baseEntity.setLastUpdateTime(time);
	}

}
