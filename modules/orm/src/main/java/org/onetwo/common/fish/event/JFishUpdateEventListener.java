package org.onetwo.common.fish.event;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JdbcStatementContext;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;

public class JFishUpdateEventListener extends UpdateEventListener {

	@Override
	protected void doUpdate(JFishUpdateEvent event, JFishMappedEntry entry){
		Object entity = event.getObject();
		JFishEventSource es = event.getEventSource();
		JdbcStatementContext<List<Object[]>> update = null;
		int count = 0;
		if(event.isDynamicUpdate()){
			if(LangUtils.isMultiple(entity)){
				Collection<Object> entityCol = CUtils.toCollection(entity);
				for(Object e : entityCol){
					update = entry.makeDymanicUpdate(e);
					count += this.executeJdbcUpdate(event, es, update);
				}
			}else{
				update = entry.makeDymanicUpdate(entity);
				count = this.executeJdbcUpdate(event, es, update);
			}
		}else{
			update = entry.makeUpdate(entity);
//			count = this.updateValues(es, update);
			count = LangUtils.sum(es.getJFishJdbcTemplate().batchUpdate(update.getSql(), update.getValue()));
		}
		event.setUpdateCount(count);
	}
	
	/*protected int updateValues(JFishEventSource es, KVEntry<String, List<Object[]>> update){
		int count = 0;
		for(Object[] args : update.getValue()){
			count += es.getJFishJdbcTemplate().updateWith(new SimpleArgsPreparedStatementCreator(update.getKey(), args), null);
			if(count==0){
				//may be try to insert
				LangUtils.throwBaseException(LangUtils.toString("no data updated: sql:[${0}],  value:${1}", update.getKey(), LangUtils.toString(args)));
			}
		}
		return count;
	}*/

}
