package org.onetwo.common.fish.relation;

import java.util.Collection;

import org.onetwo.common.fish.event.JFishDeleteEvent;
import org.onetwo.common.fish.event.JFishDeleteEventListener;
import org.onetwo.common.fish.event.JFishEventSource;
import org.onetwo.common.fish.event.MappedFieldProcessor;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.utils.LangUtils;

public class JFishRelatedDeleteEventListener extends JFishDeleteEventListener {
	
	private JFishDeleteEventListener deleteEventListener;
	
	public JFishRelatedDeleteEventListener(){
		this.deleteEventListener = new JFishDeleteEventListener();
	}

	public JFishRelatedDeleteEventListener(JFishDeleteEventListener deleteEventListener) {
		super();
		this.deleteEventListener = deleteEventListener;
	}


	@Override
	public void doDelete(final JFishDeleteEvent deleteEvent, final JFishMappedEntry entry){
		if(LangUtils.isEmpty(deleteEvent.getRelatedFields())){
			deleteEventListener.doEvent(deleteEvent);
			return ;
		}

		JFishRelatedEntryImpl jpaEntry = (JFishRelatedEntryImpl) entry;
		final JFishEventSource es = deleteEvent.getEventSource();

		
		this.processRelatedField(deleteEvent.getRelatedFields(), jpaEntry.getToManyFields(), new MappedFieldProcessor<CascadeCollectionMappedField>() {

			@Override
			public void execute(CascadeCollectionMappedField field) {
				Collection<?> relatedEntities = field.getValueAsCollection(deleteEvent.getObject());
				if(LangUtils.isEmpty(relatedEntities))
					return ;

				es.clearRef(deleteEvent.getObject(), field.getName());
				es.delete(relatedEntities, deleteEvent.getRelatedFieldsBy(field));
			}
			
		});
		
		this.processRelatedField(deleteEvent.getRelatedFields(), jpaEntry.getToOneFields(), new MappedFieldProcessor<JFishToOneMappedField>() {

			@Override
			public void execute(JFishToOneMappedField field) {
				Object relatedEntity = field.getValue(deleteEvent.getObject());
				if(relatedEntity==null)
					return;
				es.clearRef(deleteEvent.getObject(), field.getName());
				es.delete(relatedEntity, deleteEvent.getRelatedFieldsBy(field));
			}
			
		});
		
		deleteEventListener.doEvent(deleteEvent);
	}

}
