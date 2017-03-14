package org.onetwo.dbm.event;


import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.Assert;
import org.onetwo.dbm.mapping.DbmMappedEntry;
import org.onetwo.dbm.mapping.JdbcStatementContext;

public class DbmDeleteEventListener extends AbstractJFishEventListener {

	@Override
	public void doEvent(DbmEvent event) {
		DbmDeleteEvent deleteEvent = (DbmDeleteEvent) event;
		Object entity = event.getObject();
		DbmEventSource es = event.getEventSource();
		DbmMappedEntry entry = es.getMappedEntryManager().findEntry(entity!=null?entity:event.getEntityClass());
		if(entry==null)
			entry = es.getMappedEntryManager().findEntry(entity);
		Assert.notNull(entry, "can not find entry : " + event.getEntityClass()+"");
		
		this.doDelete(deleteEvent, entry);
	}
	
	public void doDelete(DbmDeleteEvent deleteEvent, DbmMappedEntry entry){
		Object entity = deleteEvent.getObject();
		DbmEventSource es = deleteEvent.getEventSource();
		
		int count = 0;
		if(deleteEvent.isDeleteAll()){
			JdbcStatementContext<Object[]> delete = entry.makeDeleteAll();
//			count = es.getJFishJdbcTemplate().update(delete.getSql(), delete.getValue());
			List<Object[]> argList = new ArrayList<>(1);
			argList.add(delete.getValue());
			count = this.executeJdbcUpdate(delete.getSql(), argList, es);
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
