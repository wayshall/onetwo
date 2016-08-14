package org.onetwo.common.jfishdbm.event;


import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.JdbcStatementContext;
import org.onetwo.common.utils.Assert;

public class JFishDeleteEventListener extends AbstractJFishEventListener {

	@Override
	public void doEvent(JFishEvent event) {
		JFishDeleteEvent deleteEvent = (JFishDeleteEvent) event;
		Object entity = event.getObject();
		JFishEventSource es = event.getEventSource();
		JFishMappedEntry entry = es.getMappedEntryManager().findEntry(entity!=null?entity:event.getEntityClass());
		if(entry==null)
			entry = es.getMappedEntryManager().findEntry(entity);
		Assert.notNull(entry, "can not find entry : " + event.getEntityClass()+"");
		
		this.doDelete(deleteEvent, entry);
	}
	
	public void doDelete(JFishDeleteEvent deleteEvent, JFishMappedEntry entry){
		Object entity = deleteEvent.getObject();
		JFishEventSource es = deleteEvent.getEventSource();
		
		int count = 0;
		if(deleteEvent.isDeleteAll()){
			JdbcStatementContext<Object[]> delete = entry.makeDeleteAll();
//			count = es.getJFishJdbcTemplate().update(delete.getSql(), delete.getValue());
			List<Object[]> argList = new ArrayList<>(1);
			argList.add(delete.getValue());
			this.executeJdbcUpdate(delete.getSql(), argList, es);
		}else{
			JdbcStatementContext<List<Object[]>> delete = entry.makeDelete(entity, deleteEvent.isDeleteByIdentify());
			List<List<Object[]>> argList = new ArrayList<>(1);
			argList.add(delete.getValue());
			count = this.executeJdbcUpdate(delete.getSql(), delete.getValue(), es);
		}
		/*if(count<1)
			throw new JFishOrmException("can not delete any entity["+entry.getEntityClass()+"] : " + count);*/
		deleteEvent.setUpdateCount(count);
	}

}
