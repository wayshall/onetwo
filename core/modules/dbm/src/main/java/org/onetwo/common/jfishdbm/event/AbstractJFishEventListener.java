package org.onetwo.common.jfishdbm.event;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.jfishdbm.exception.DbmException;
import org.onetwo.common.jfishdbm.jdbc.SimpleArgsPreparedStatementCreator;
import org.onetwo.common.jfishdbm.mapping.DataBaseConfig;
import org.onetwo.common.jfishdbm.mapping.DbmMappedField;
import org.onetwo.common.jfishdbm.mapping.EntrySQLBuilder;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntryMeta;
import org.onetwo.common.jfishdbm.mapping.JdbcStatementContext;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

@SuppressWarnings("unchecked")
abstract public class AbstractJFishEventListener implements JFishEventListener {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
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
				throwIfMultiple(entities, obj);
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
	
	protected void throwIfMultiple(Object parent, Object entity){
		if(LangUtils.isMultiple(entity)){
			String msg = "element of "+(parent==null?"container":parent)+" can not be a multiple object, element: " + entity;
			throw new DbmException(msg);
		}
	}

	
	protected <T extends DbmMappedField> void processRelatedField(String[] relatedFields, Collection<? extends DbmMappedField> mappedFields, MappedFieldProcessor<T> processor){
		for(DbmMappedField field : mappedFields){
			if(ArrayUtils.contains(relatedFields, field.getName())){
				processor.execute((T)field);
			}
		}
	}
	
	protected boolean isUseBatchUpdate(List<?> args, JFishEventSource es){
		DataBaseConfig dbc = es.getDataBaseConfig();
		return dbc.isBatchEnabled() && args.size()>dbc.getUseBatchThreshold();
	}
	

	protected int executeJdbcUpdate(JFishEventSource es, JdbcStatementContext<List<Object[]>> update){
		return executeJdbcUpdate(update.getSql(), update.getValue(), es);
	}
	
	/********
	 * 会更加配置决定是否调用jdbc的executeBatch接口
	 * @param event
	 * @param sql
	 * @param args
	 * @param es
	 * @return
	 */
	protected int executeJdbcUpdate(String sql, List<Object[]> args, JFishEventSource es){
		return executeJdbcUpdate(isUseBatchUpdate(args, es), sql, args, es);
	}
	
	/******
	 * 如果使用批量处理，因为某些驱动的实现机制，比如oracle，不能根据返回值来判断是否更新成功
	 * @param userBatch
	 * @param sql
	 * @param args
	 * @param es
	 * @return
	 */
	protected int executeJdbcUpdate(boolean userBatch, String sql, List<Object[]> args, JFishEventSource es){
		int count = 0;
		if(userBatch){
//			int[] ups = es.getJFishJdbcTemplate().batchUpdate(sql, args);
			int batchSize = es.getDataBaseConfig().getProcessSizePerBatch();
			int[][] ups = es.getJFishJdbcTemplate().batchUpdateWith(sql, args, batchSize/*, new ParameterizedPreparedStatementSetter<Object[]>(){

				@Override
				public void setValues(PreparedStatement ps, Object[] argument) throws SQLException {
					JdbcUtils.setValues(ps, argument);
				}
				
			}*/);
			for(int[] up : ups)
				count += LangUtils.sum(up);
		}else{
			for(Object[] arg : args){
				count += es.getJFishJdbcTemplate().updateWith(new SimpleArgsPreparedStatementCreator(sql, arg));
			}
		}
		return count;
	}
	

	protected void executeJFishEntityListener(boolean before, JFishEvent jfishEvent, Object entities, List<DbmEntityListener> listeners){
		if(LangUtils.isMultiple(entities)){
			List<?> list = LangUtils.asList(entities);
			for(Object entity : list){
				this.executeJFishEntityListenerForSingle(before, jfishEvent, entity, listeners);
			}
		}else{
			this.executeJFishEntityListenerForSingle(before, jfishEvent, entities, listeners);
		}
	}

	protected void executeJFishEntityListenerForSingle(boolean before, JFishEvent jfishEvent, Object entity, List<DbmEntityListener> listeners){
		if(LangUtils.isEmpty(listeners))
			return ;
		if(before){
			if(jfishEvent.getAction()==JFishEventAction.insert){
				for(DbmEntityListener jel : listeners){
					jel.beforeInsert(entity);
				}
			}
			else if(jfishEvent.getAction()==JFishEventAction.update){
				for(DbmEntityListener jel : listeners){
					jel.beforeUpdate(entity);
				}
			}
		}else{
			if(jfishEvent.getAction()==JFishEventAction.insert){
				for(DbmEntityListener jel : listeners){
					jel.afterInsert(entity);
				}
			}
			else if(jfishEvent.getAction()==JFishEventAction.update){
				for(DbmEntityListener jel : listeners){
					jel.afterUpdate(entity);
				}
			}
		}
	}
	

	protected void updateEntityVersionIfNecessary(EntrySQLBuilder builder, Object[] updateValues, Object singleEntity){
		JFishMappedEntryMeta entry = builder.getEntry();
		if(entry.isVersionControll()){
			Object versionValue = builder.getVersionValue(updateValues);
			entry.getVersionField().setValue(singleEntity, versionValue);
		}
	}
}
