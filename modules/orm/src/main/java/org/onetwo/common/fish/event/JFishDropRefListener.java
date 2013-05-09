package org.onetwo.common.fish.event;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedFieldType;
import org.onetwo.common.fish.relation.CascadeCollectionMappedField;
import org.onetwo.common.fish.relation.CascadeMappedField;
import org.onetwo.common.utils.map.KVEntry;

public class JFishDropRefListener extends JFishMaintainRefListener {

	@Override
	protected void dropRef(JFishDropRefEvent event, JFishMappedEntry entry, CascadeMappedField cfield, Object entity){
		int updateCount = 0;
		if(event.isDropAllRef()){//clear
			updateCount = this.dropAllRef(event, entry, cfield, entity);
		}else{
			updateCount = this.dropRelatedRef(event, entry, cfield, entity);
		}
		event.setUpdateCount(updateCount);
	}
	

	protected int dropAllRef(JFishDropRefEvent event, JFishMappedEntry entry, CascadeMappedField cfield, Object entity){
		KVEntry<String, Object[]> values = null;
		int updateCount = 0;
		if(cfield.getMappedFieldType()==JFishMappedFieldType.TO_ONE){
			if(cfield.isJoinTableField()){
				values = cfield.getJoinTableMapper().makeDropAllRef(entity);
			}else{
				values = cfield.getJoinColumnMapper().makeDropAllRef(cfield.getValue(entity));
			}
			updateCount = event.getEventSource().getJFishJdbcTemplate().update(values.getKey(), values.getValue());
			
		}else{
			if(cfield.isJoinTableField()){
				values = cfield.getJoinTableMapper().makeDropAllRef(entity);
			}else{
//				Collection<?> relatedEntities = ((CascadeCollectionMappedField)cfield).getValueAsCollection(entity);
				values = cfield.getJoinColumnMapper().makeDropAllRef(entity);
			}
//			int[] batchUpdate = event.getEventSource().getJFishJdbcTemplate().batchUpdate(values.getKey(), values.getValue());
//			for(int u : batchUpdate){
//				updateCount += u;
//			}
			updateCount = event.getEventSource().getJFishJdbcTemplate().update(values.getKey(), values.getValue());
		}
		
		return updateCount;
	}
	
	protected int dropRelatedRef(JFishDropRefEvent event, JFishMappedEntry entry, CascadeMappedField cfield, Object entity){
		int updateCount = 0;
		if(cfield.getMappedFieldType()==JFishMappedFieldType.TO_ONE){
			KVEntry<String, Object[]> values = null;
			if(cfield.isJoinTableField()){
				values = cfield.getJoinTableMapper().makeDropRefOfToOne(entity);
//				values = cfield.getJoinTableMapper().makeDropAllRef(entity);
			}else{
				values = cfield.getJoinColumnMapper().makeDropRefOfToOne(entity);
			}
			updateCount = event.getEventSource().getJFishJdbcTemplate().update(values.getKey(), values.getValue());
		}else{
			KVEntry<String, List<Object[]>> values = null;
			if(cfield.isJoinTableField()){
				values = cfield.getJoinTableMapper().makeDropRefOfToMany(entity);
			}else{
				Collection<?> relatedEntities = ((CascadeCollectionMappedField)cfield).getValueAsCollection(entity);
				values = cfield.getJoinColumnMapper().makeSaveRefOfToMany(entity, relatedEntities);
			}
			int[] batchUpdate = event.getEventSource().getJFishJdbcTemplate().batchUpdate(values.getKey(), values.getValue());
			for(int u : batchUpdate){
				updateCount += u;
			}
		}
		return updateCount;
	}

}
