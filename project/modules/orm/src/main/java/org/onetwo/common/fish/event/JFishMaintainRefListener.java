package org.onetwo.common.fish.event;

import java.util.List;

import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.relation.CascadeMappedField;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

abstract public class JFishMaintainRefListener extends AbstractJFishEventListener {


	@Override
	public void doEvent(JFishEvent event) {
		JFishEventSource es = event.getEventSource();
		JFishMappedEntry entry = es.getMappedEntryManager().getEntry(event.getObject());
		if(LangUtils.isEmpty(event.getRelatedFields()))
			return ;
		
		List<Object> entities = LangUtils.asList(event.getObject());
		for(Object entity : entities){
			if(!entry.hasIdentifyValue(entity)){
				throw new JFishOrmException("entity["+entry.getEntityName()+"] has not saved yet!");
			}
			CascadeMappedField cfield = null;
			for(String fieldName : event.getRelatedFields()){
				JFishMappedField field = entry.getField(fieldName);
				if(!CascadeMappedField.class.isInstance(field))
					throw new JFishOrmException("it's not cascade field : " + fieldName);
				
				cfield = (CascadeMappedField)field;
				Assert.notNull(cfield.getMappedFieldType());
				if(event.getAction()==JFishEventAction.saveRef){
					this.saveRef((JFishSaveRefEvent)event, entry, cfield, entity);
				}else{
					this.dropRef((JFishDropRefEvent)event, entry, cfield, entity);
				}
			}
		}
	}
	
	protected void saveRef(JFishSaveRefEvent event, JFishMappedEntry entry, CascadeMappedField field, Object entity){
	}
	
	protected void dropRef(JFishDropRefEvent event, JFishMappedEntry entry, CascadeMappedField field, Object entity){
	}

}
