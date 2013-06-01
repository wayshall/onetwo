package org.onetwo.common.fish.event;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedFieldType;
import org.onetwo.common.fish.relation.CascadeCollectionMappedField;
import org.onetwo.common.fish.relation.CascadeMappedField;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.map.KVEntry;

public class JFishSaveRefListener extends JFishMaintainRefListener {

	@Override
	protected void saveRef(JFishSaveRefEvent event, JFishMappedEntry entry, CascadeMappedField cfield, Object entity){
		int updateCount = 0;
		
		if(cfield.getMappedFieldType()==JFishMappedFieldType.TO_ONE){
			KVEntry<String, Object[]> values = null;
			if(cfield.isJoinTableField()){
//				updateCount += event.getEventSource().clearRef(entity, cfield.getName());
				values = cfield.getJoinTableMapper().makeDropRefOfToOne(entity);
				updateCount += event.getEventSource().getJFishJdbcTemplate().update(values.getKey(), values.getValue());
				
				values = cfield.getJoinTableMapper().makeSaveRefOfToOne(entity);
			}else{
				values = cfield.getJoinColumnMapper().makeSaveRefOfToOne(entity);
			}
			
			if(values==null)
				return ;
			
			updateCount += event.getEventSource().getJFishJdbcTemplate().update(values.getKey(), values.getValue());
		}else{
			if(event.isDropAllInFirst()){
				updateCount = event.getEventSource().clearRef(entity, cfield.getName());
				logger.info("drop many ref count : " + updateCount);
			}
			
			KVEntry<String, List<Object[]>> values = null;
			if(cfield.isJoinTableField()){
				values = cfield.getJoinTableMapper().makeSaveRefOfToMany(entity);
				
			}else{
				Collection<?> relatedEntities = ((CascadeCollectionMappedField)cfield).getValueAsCollection(entity);
				values = cfield.getJoinColumnMapper().makeSaveRefOfToMany(entity, relatedEntities);
			}
			
			if(values==null)
				return ;
			
			int[] ups = event.getEventSource().getJFishJdbcTemplate().batchUpdate(values.getKey(), values.getValue());
			updateCount += LangUtils.sum(ups);
		}
		event.setUpdateCount(updateCount);
	}

}
