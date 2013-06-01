package org.onetwo.common.fish.event;

import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.utils.LangUtils;

public class JFishBatchUpdateEventListener extends UpdateEventListener {

	@Override
	public void doEvent(JFishEvent event) {
		JFishMappedEntry entry = event.getEventSource().getMappedEntryManager().findEntry(event.getObject());
		if(entry==null){
			event.setUpdateCount(0);
			return ;
		}
		super.doEvent(event);
	}
	
	@Override
	protected void doUpdate(JFishUpdateEvent event, JFishMappedEntry entry){
		Object entity = event.getObject();
		if(!LangUtils.isMultiple(entity)){
			throw new JFishOrmException("batch update's args must be a Collection or Array!");
		}
		int count = this.executeJdbcUpdate(event.getEventSource(), entry.makeUpdate(entity));
		event.setUpdateCount(count);
	}

}
