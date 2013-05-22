package org.onetwo.common.fish.event;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.fish.orm.DataBaseConfig;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.orm.JdbcStatementContext;
import org.onetwo.common.jdbc.JdbcUtils;
import org.onetwo.common.jdbc.SimpleArgsPreparedStatementCreator;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

@SuppressWarnings("unchecked")
abstract public class AbstractJFishEventListener implements JFishEventListener {
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	/*public void doEvent(JFishEvent event){
		this.onInnerEvent(event);
	}*/
	
//	abstract protected void onInnerEvent(JFishEvent event);
	

	public void doEvent(JFishEvent event) {
		Object entity = event.getObject();
		JFishInsertOrUpdateEvent insertOrUpdate = (JFishInsertOrUpdateEvent) event;

		int updateCount = 0;
		if(LangUtils.isMultiple(entity)){
			Collection<Object> entities = CUtils.toCollection(entity);
			for(Object obj : entities){
				if(obj==null)
					continue;
				updateCount += this.onInnerEventWithSingle(obj, insertOrUpdate);
			}
		}else{
			updateCount = onInnerEventWithSingle(entity, insertOrUpdate);
		}
		event.setUpdateCount(updateCount);
	}
	
	protected int onInnerEventWithSingle(Object entity, JFishEvent event){
		throw new UnsupportedOperationException();
	}

	
	protected <T extends JFishMappedField> void processRelatedField(String[] relatedFields, Collection<? extends JFishMappedField> mappedFields, MappedFieldProcessor<T> processor){
		for(JFishMappedField field : mappedFields){
			if(ArrayUtils.contains(relatedFields, field.getName())){
				processor.execute((T)field);
			}
		}
	}
	
	protected boolean isUseBatchUpdate(List<?> args, JFishEventSource es){
		DataBaseConfig dbc = es.getDialect().getDataBaseConfig();
		return dbc.isBatchEnabled() && args.size()>dbc.getUserBatchThreshold();
	}
	

	protected int executeJdbcUpdate(JFishEvent event, JFishEventSource es, JdbcStatementContext<List<Object[]>> update){
		return executeJdbcUpdate(event, update.getSql(), update.getValue(), es);
	}
	
	/********
	 * 会更加配置决定是否调用jdbc的executeBatch接口
	 * @param event
	 * @param sql
	 * @param args
	 * @param es
	 * @return
	 */
	protected int executeJdbcUpdate(JFishEvent event, String sql, List<Object[]> args, JFishEventSource es){
		return executeJdbcUpdate(isUseBatchUpdate(args, es), event, sql, args, es);
	}
	
	protected int executeJdbcUpdate(boolean userBatch, JFishEvent event, String sql, List<Object[]> args, JFishEventSource es){
		int count = 0;
		if(userBatch){
//			int[] ups = es.getJFishJdbcTemplate().batchUpdate(sql, args);
			int batchSize = es.getDialect().getDataBaseConfig().getBatchSizeForUpdate();
			int[][] ups = es.getJFishJdbcTemplate().batchUpdate(sql, args, batchSize, new ParameterizedPreparedStatementSetter<Object[]>(){

				@Override
				public void setValues(PreparedStatement ps, Object[] argument) throws SQLException {
					JdbcUtils.setValues(ps, argument);
				}
				
			});
			for(int[] up : ups)
				count += LangUtils.sum(up);
		}else{
			for(Object[] arg : args){
				count += es.getJFishJdbcTemplate().updateWith(new SimpleArgsPreparedStatementCreator(sql, arg), null);
			}
		}
		return count;
	}
	

	protected void executeJFishEntityListener(boolean before, JFishEvent jfishEvent, Object entities, List<JFishEntityListener> listeners){
		if(LangUtils.isMultiple(entities)){
			List<?> list = LangUtils.asList(entities);
			for(Object entity : list){
				this.executeJFishEntityListenerForSingle(before, jfishEvent, entity, listeners);
			}
		}else{
			this.executeJFishEntityListenerForSingle(before, jfishEvent, entities, listeners);
		}
	}

	protected void executeJFishEntityListenerForSingle(boolean before, JFishEvent jfishEvent, Object entity, List<JFishEntityListener> listeners){
		if(LangUtils.isEmpty(listeners))
			return ;
		if(before){
			if(jfishEvent.getAction()==JFishEventAction.insert){
				for(JFishEntityListener jel : listeners){
					jel.beforeInsert(entity);
				}
			}
			else if(jfishEvent.getAction()==JFishEventAction.update){
				for(JFishEntityListener jel : listeners){
					jel.beforeUpdate(entity);
				}
			}
		}else{
			if(jfishEvent.getAction()==JFishEventAction.insert){
				for(JFishEntityListener jel : listeners){
					jel.afterInsert(entity);
				}
			}
			else if(jfishEvent.getAction()==JFishEventAction.update){
				for(JFishEntityListener jel : listeners){
					jel.afterUpdate(entity);
				}
			}
		}
	}
}
