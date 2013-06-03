package org.onetwo.common.fish.relation;

import java.util.Collection;

import org.onetwo.common.fish.event.JFishEvent;
import org.onetwo.common.fish.event.JFishEventSource;
import org.onetwo.common.fish.event.JFishInsertOrUpdateEvent;
import org.onetwo.common.fish.event.JFishInsertOrUpdateListener;
import org.onetwo.common.fish.event.MappedFieldProcessor;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.utils.LangUtils;

public class JFishRelatedInsertOrUpdateEventListener extends JFishInsertOrUpdateListener {

	private JFishInsertOrUpdateListener insertOrUpdateEventListener;

	public JFishRelatedInsertOrUpdateEventListener() {
		this.insertOrUpdateEventListener = new JFishInsertOrUpdateListener();
	}

	public JFishRelatedInsertOrUpdateEventListener(JFishInsertOrUpdateListener updateEventListener) {
		super();
		this.insertOrUpdateEventListener = updateEventListener;
	}

	@Override
	public void doEvent(JFishEvent event) {
		if (LangUtils.isEmpty(event.getRelatedFields())) {
			insertOrUpdateEventListener.doEvent(event);
			return;
		}

		JFishMappedEntry entry = event.getEventSource().getMappedEntryManager().getEntry(event.getObject());
		final JFishInsertOrUpdateEvent insertOrUpdate = (JFishInsertOrUpdateEvent) event;
		final JFishEventSource es = insertOrUpdate.getEventSource();
		
		final JFishRelatedEntryImpl jpaEntry = (JFishRelatedEntryImpl) entry;
		this.processRelatedField(insertOrUpdate.getRelatedFields(), jpaEntry.getToOneFields(), new MappedFieldProcessor<CascadeMappedField>() {

			@Override
			public void execute(CascadeMappedField field) {
				Object relatedEntity = field.getValue(insertOrUpdate.getObject());
				if (relatedEntity == null)
					return;

				es.insertOrUpdate(relatedEntity, insertOrUpdate.isDynamicUpdate(), insertOrUpdate.getRelatedFieldsBy(field));
//				es.saveRef(insertOrUpdate.getObject(), false, field.getName());
			}

		});
		
		es.insertOrUpdate(insertOrUpdate.getObject(), insertOrUpdate.isDynamicUpdate());
		this.processRelatedField(insertOrUpdate.getRelatedFields(), jpaEntry.getToOneFields(), new MappedFieldProcessor<CascadeMappedField>() {
			@Override
			public void execute(CascadeMappedField field) {
				es.saveRef(insertOrUpdate.getObject(), false, field.getName());
			}
		});

		this.processRelatedField(insertOrUpdate.getRelatedFields(), jpaEntry.getToManyFields(), new MappedFieldProcessor<CascadeCollectionMappedField>() {

			@Override
			public void execute(CascadeCollectionMappedField field) {
				Collection<?> relatedEntities = field.getValueAsCollection(insertOrUpdate.getObject());
				if (LangUtils.isEmpty(relatedEntities))
					return;

				es.insertOrUpdate(relatedEntities, insertOrUpdate.isDynamicUpdate(), insertOrUpdate.getRelatedFieldsBy(field));
				es.saveRef(insertOrUpdate.getObject(), false, field.getName());
			}

		});
	}

}
