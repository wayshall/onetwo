package org.onetwo.common.jfishdbm.event;

import org.onetwo.common.jfishdbm.exception.DbmException;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
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
			throw new DbmException("batch update's args must be a Collection or Array!");
		}
		int count = this.executeJdbcUpdate(event.getEventSource(), entry.makeUpdate(entity));
		event.setUpdateCount(count);
	}

}
