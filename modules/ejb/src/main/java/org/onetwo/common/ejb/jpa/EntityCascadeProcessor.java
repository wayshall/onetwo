package org.onetwo.common.ejb.jpa;

import java.beans.PropertyDescriptor;
import java.util.Collection;

import javax.persistence.EntityManager;

import org.onetwo.common.db.IdEntity;
import org.onetwo.common.db.event.EntityAction;
import org.onetwo.common.db.event.EventSource;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.annotation.AbstractAnnotationProcessor;
import org.onetwo.common.utils.annotation.AnnoContext;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EntityCascadeProcessor extends AbstractAnnotationProcessor<EntityAction> {
	
	public static EntityCascadeProcessor create(EntityAction action){ 
		return new EntityCascadeProcessor(action);
	}
	
	public EntityCascadeProcessor(EntityAction action) {
		super(action);
	}
 
	@Override
	public void doAnnotation(AnnoContext context) {
//		EntityCascade cascade = (EntityCascade)context.getAnnotation();
		/*if(!canDoAnnotation(cascade))
			return ;*/
//		EntityAction[] cascades = cascade.action();
		
		Object entity = context.getSrcObject();
		JPAAnnoContext jpaContext = (JPAAnnoContext) context;
		EventSource es = jpaContext.getEvent().getEventSource();
		EntityManager em = es.getEntityManager();
		
		PropertyDescriptor p = jpaContext.getAnnoIn();
		Object fieldValue = ReflectUtils.invokeMethod(p.getReadMethod(), entity);
		Collection<IdEntity> values = LangUtils.emptyIfNull(LangUtils.asList(fieldValue));
		
		for(IdEntity o : values){
			if(EntityAction.Remove.equals(context.getEventAction())){
				LangUtils.println("remove : " + o);
				es.remove(o);
				em.flush();
			}else if(EntityAction.SaveOrUpdate.equals(context.getEventAction())){
				LangUtils.println("SaveOrUpdate : " + o);
				es.save(o);
				em.flush();
			}
		}
		
	}

}
