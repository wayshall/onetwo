package org.onetwo.common.fish.event;


import java.util.List;

import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JdbcStatementContext;
import org.onetwo.common.utils.LangUtils;

/*******
 * 和普通insert的区别只在，当实体的id策略是自增时，不会通过Statement#getGeneratedKeys接口获取数据库递增的id值和回写到实体的id
 * 只对mysql有效
 * @author wayshall
 *
 */
public class JFishBatchInsertEventListener extends JFishInsertEventListener{

	@Override
	protected void doInsert(JFishInsertEvent event, JFishMappedEntry entry) {
		Object entity = event.getObject();
		if(!LangUtils.isMultiple(entity)){
			throw new JFishOrmException("batch insert's args must be a Collection or Array!");
		}
		JFishEventSource es = event.getEventSource();
		this.beforeDoInsert(event, entry);
		this.batchInsert(event, entry, es);
	}
	
	protected void batchInsert(JFishInsertEvent event, JFishMappedEntry entry, JFishEventSource es) {
		Object entity = event.getObject();
		
		JdbcStatementContext<List<Object[]>> insert = entry.makeInsert(entity);
//		int[] counts = es.getJFishJdbcTemplate().batchUpdate(insert.getKey(), insert.getValue());
//		int total = LangUtils.sum(counts);
		int total = this.executeJdbcUpdate(true, event, insert.getSql(), insert.getValue(), es);
		event.setUpdateCount(total);
	}

}
