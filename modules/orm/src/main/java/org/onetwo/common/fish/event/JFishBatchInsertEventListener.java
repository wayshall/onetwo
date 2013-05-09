package org.onetwo.common.fish.event;


import java.util.List;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JdbcStatementContext;

public class JFishBatchInsertEventListener extends JFishInsertEventListener{

	@Override
	protected void doInsert(JFishInsertEvent event, JFishMappedEntry entry) {
		JFishEventSource es = event.getEventSource();
		this.beforeDoInsert(event, entry);
		this.batchInsert(event, entry, es);
	}
	
	protected void batchInsert(JFishInsertEvent event, JFishMappedEntry entry, JFishEventSource es) {
		Object entity = event.getObject();
		
		JdbcStatementContext<List<Object[]>> insert = entry.makeInsert(entity);
//		int[] counts = es.getJFishJdbcTemplate().batchUpdate(insert.getKey(), insert.getValue());
//		int total = LangUtils.sum(counts);
		int total = this.executeJdbcUpdate(event, es, insert);
		event.setUpdateCount(total);
	}

}
