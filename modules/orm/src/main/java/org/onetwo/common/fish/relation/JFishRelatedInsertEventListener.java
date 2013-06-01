package org.onetwo.common.fish.relation;

import org.onetwo.common.fish.event.InsertEventListener;
import org.onetwo.common.fish.event.JFishEventSource;
import org.onetwo.common.fish.event.JFishInsertEvent;
import org.onetwo.common.fish.event.JFishInsertEventListener;
import org.onetwo.common.fish.event.MappedFieldProcessor;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.relation.JFishOneToManyMappedField.FieldValueHolder;
import org.onetwo.common.utils.LangUtils;

public class JFishRelatedInsertEventListener extends InsertEventListener {
	
	private InsertEventListener insertEventListener;
	
	
	public JFishRelatedInsertEventListener(){
		this.insertEventListener = new JFishInsertEventListener();
	}
	
	public JFishRelatedInsertEventListener(InsertEventListener insertEventListener) {
		super();
		this.insertEventListener = insertEventListener;
	}

	@Override
	protected void doInsert(final JFishInsertEvent event, final JFishMappedEntry entry) {
//		insertEventListener.doEvent(event);
		if(LangUtils.isEmpty(event.getRelatedFields())){
			insertEventListener.doEvent(event);
			return ;
		}

		final JFishEventSource es = event.getEventSource();
		final JFishRelatedEntryImpl jpaEntry = (JFishRelatedEntryImpl) entry;
		
		
		this.processRelatedField(event.getRelatedFields(), jpaEntry.getToOneFields(), new MappedFieldProcessor<CascadeMappedField>() {

			@Override
			public void execute(CascadeMappedField field) {
				Object relatedEntity = field.getValue(event.getObject());
				if(relatedEntity==null)
					return ;
				
				JFishMappedEntry relatedEntry = es.getMappedEntryManager().getEntry(relatedEntity);
				//ignore if entity has a id value
				if(relatedEntry.hasIdentifyValue(relatedEntity)){
//					es.insert(relatedEntity, event.getRelatedFieldsBy(field));
					//Ignore
				}else{
					es.insert(relatedEntity, event.getRelatedFieldsBy(field));
				}
//				es.saveRef(event.getObject(), field.getName());
			}
			
		});

		es.insert(event.getObject());
		
		//saveRef
		this.processRelatedField(event.getRelatedFields(), jpaEntry.getToOneFields(), new MappedFieldProcessor<CascadeMappedField>() {
			@Override
			public void execute(CascadeMappedField field) {
				es.saveRef(event.getObject(), field.getName());
			}
		});
		
		//many
		this.processRelatedField(event.getRelatedFields(), jpaEntry.getToManyFields(), new MappedFieldProcessor<CascadeCollectionMappedField>() {

			@Override
			public void execute(CascadeCollectionMappedField field) {
//				JFishMappedField inverseField = null;
//				Collection<?> relatedEntities = field.getValueAsCollection(event.getObject());
				FieldValueHolder fholder = field.getValueAsValueHolder(event.getObject());
				if(fholder.isEmpty())
					return ;

				//insert news only
				if(!fholder.news.isEmpty()){
					es.insert(fholder.news, event.getRelatedFieldsBy(field));
					es.saveRef(event.getObject(), field.getName());
				}
			}
			
		});
		
	}


}
