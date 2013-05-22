package org.onetwo.common.fish.event;

import java.util.List;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JdbcStatementContext;
import org.onetwo.common.jdbc.SimpleArgsPreparedStatementCreator;
import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/*******
 * 
 * @author wayshall
 *
 */
public class JFishInsertEventListener extends InsertEventListener{

	protected void beforeDoInsert(JFishInsertEvent event, JFishMappedEntry entry){
	}
	
	protected void doInsert(JFishInsertEvent event, JFishMappedEntry entry) {
		JFishEventSource es = event.getEventSource();
		this.beforeDoInsert(event, entry);
		
		Object entity = event.getObject();
		
		JdbcStatementContext<List<Object[]>> insert = entry.makeInsert(entity);
		if(insert==null)
			return ;
		String sql = insert.getSql();
		List<Object[]> args = insert.getValue();
		List<Object> objects = LangUtils.asList(entity);
		
		int updateCount = 0;
		if(event.isFetchId()){
			if(entry.getIdentifyField()!=null && entry.getIdentifyField().isIncreaseIdStrategy()){ 
				int index = 0;
				for(Object[] arg : args){
					KeyHolder keyHolder = new GeneratedKeyHolder();
					updateCount += es.getJFishJdbcTemplate().updateWithKeyHolder(new SimpleArgsPreparedStatementCreator(sql, arg), keyHolder);
					if(keyHolder.getKey()!=null)
						entry.setId(objects.get(index++), keyHolder.getKey());
				}
			}else{
				updateCount += executeJdbcUpdate(sql, args, es);
			}
		}else{
			updateCount += executeJdbcUpdate(sql, args, es);
		}

		/*if(updateCount<1)
			throw new JFishOrmException("can not insert any entity["+entry.getEntityClass()+"] : " + updateCount);*/
		
		event.setUpdateCount(updateCount);
	}
	
}
