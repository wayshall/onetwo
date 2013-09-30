package org.onetwo.common.ejb.jpa;

import javax.persistence.EntityManager;

import org.onetwo.common.db.anno.EntityCascade;
import org.onetwo.common.db.event.DefaultEventListener;
import org.onetwo.common.db.event.EntityAction;
import org.onetwo.common.db.event.RemoveEvent;
import org.onetwo.common.db.event.RemoveEventListener;

public class JPARemoveEventListener extends DefaultEventListener implements RemoveEventListener {

	/*public JPARemoveEventListener(AnnotationProcessorManager annotationProcessorManager) {
		super(annotationProcessorManager);
	}*/

	@Override
	public void init() {
		super.init();
		registerProcessor(EntityCascade.class, EntityCascadeProcessor.create(EntityAction.Remove));
	}
	
	@Override
	public void onRemove(RemoveEvent event) {
		EntityManager em = event.getEventSource().getEntityManager();
		Object entity = em.merge(event.getObject());
		
		JPAAnnoContext context = new JPAAnnoContext(entity, event.getAction());
		context.setEvent(event);
		autoProcess(context);
		
		em.remove(entity);
	}
}
