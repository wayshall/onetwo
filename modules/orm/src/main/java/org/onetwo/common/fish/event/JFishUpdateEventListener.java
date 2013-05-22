package org.onetwo.common.fish.event;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.fish.exception.JFishEntityNotFoundException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JdbcStatementContext;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;

public class JFishUpdateEventListener extends UpdateEventListener {

	@Override
	protected void doUpdate(JFishUpdateEvent event, JFishMappedEntry entry){
		Object entity = event.getObject();
		JFishEventSource es = event.getEventSource();
//		JdbcStatementContext<List<Object[]>> update = null;
		int count = 0;
		if(event.isDynamicUpdate()){
			if(LangUtils.isMultiple(entity)){
				Collection<Object> entityCol = CUtils.toCollection(entity);
				for(Object e : entityCol){
					throwIfMultiple(entity, e);
					count += updateSingleEntity(true, es, entry, e);
				}
			}else{
				count += this.updateSingleEntity(true, es, entry, entity);
			}
		}else{
			if(event.isBatchUpdate()){
				count = this.executeJdbcUpdate(es, entry.makeUpdate(entity));
			}else{
				count = this.updateSingleEntity(false, es, entry, entity);
			}
		}
		event.setUpdateCount(count);
	}

	private int updateSingleEntity(boolean dymanic, JFishEventSource es, JFishMappedEntry entry, Object entity){
		JdbcStatementContext<List<Object[]>> update = dymanic?entry.makeDymanicUpdate(entity):entry.makeUpdate(entity);
		int count = this.executeJdbcUpdate(false, update.getSql(), update.getValue(), es);
		
		if(count<1)
			throw new JFishEntityNotFoundException("update count is " + count + ".", entity.getClass(), entry.getId(entity));
		return count;
	}

}
