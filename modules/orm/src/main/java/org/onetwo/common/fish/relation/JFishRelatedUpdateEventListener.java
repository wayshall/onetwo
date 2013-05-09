package org.onetwo.common.fish.relation;

import org.onetwo.common.fish.event.JFishEventSource;
import org.onetwo.common.fish.event.JFishUpdateEvent;
import org.onetwo.common.fish.event.JFishUpdateEventListener;
import org.onetwo.common.fish.event.MappedFieldProcessor;
import org.onetwo.common.fish.event.UpdateEventListener;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.relation.JFishOneToManyMappedField.FieldValueHolder;
import org.onetwo.common.utils.LangUtils;

public class JFishRelatedUpdateEventListener extends UpdateEventListener {
	
	private UpdateEventListener updateEventListener;
	
	
	public JFishRelatedUpdateEventListener(){
		this.updateEventListener = new JFishUpdateEventListener();
	}
	
	public JFishRelatedUpdateEventListener(UpdateEventListener updateEventListener) {
		super();
		this.updateEventListener = updateEventListener;
	}

	@Override
	protected void doUpdate(final JFishUpdateEvent event, final JFishMappedEntry entry){
//		updateEventListener.doEvent(event);
		if(LangUtils.isEmpty(event.getRelatedFields())){
			updateEventListener.doEvent(event);
			return ;
		}
//		Object entity = event.getObject();
		final JFishEventSource es = event.getEventSource();
		
		es.update(event.getObject());
		
		final JFishRelatedEntryImpl jpaEntry = (JFishRelatedEntryImpl) entry;
		this.processRelatedField(event.getRelatedFields(), jpaEntry.getToOneFields(), new MappedFieldProcessor<CascadeMappedField>() {

			@Override
			public void execute(CascadeMappedField field) {
				Object relatedEntity = field.getValue(event.getObject());
				if(relatedEntity==null)
					return ;
				if(LangUtils.isMultiple(relatedEntity)){
					throw new JFishOrmException("one field["+entry.getEntityName()+"."+field.getName()+"] cannot be a collection object : " + relatedEntity);
				}
				JFishMappedEntry rentry = es.getMappedEntryManager().getEntry(relatedEntity);
				if(rentry.hasIdentifyValue(relatedEntity)){
					es.update(relatedEntity, event.isDynamicUpdate(), event.getRelatedFieldsBy(field));
					es.saveRef(event.getObject(), true, field.getName());
				}else{
					//ignore
				}
			}
			
		});
		

		this.processRelatedField(event.getRelatedFields(), jpaEntry.getToManyFields(), new MappedFieldProcessor<CascadeCollectionMappedField>() {

			@Override
			public void execute(CascadeCollectionMappedField field) {
//				Collection<?> relatedEntities = field.getValueAsCollection(event.getObject());
				FieldValueHolder fholder = field.getValueAsValueHolder(event.getObject());
				if(fholder.isEmpty())
					return ;

				if(!fholder.saveds.isEmpty()){
					es.update(fholder.saveds, event.isDynamicUpdate(), event.getRelatedFieldsBy(field));
					es.saveRef(event.getObject(), true, field.getName());
				}
				
			}
			
		});
	}


}
