package org.onetwo.dbm.event;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.exception.EntityNotFoundException;
import org.onetwo.dbm.exception.EntityVersionException;
import org.onetwo.dbm.mapping.DbmMappedField;
import org.onetwo.dbm.mapping.JFishMappedEntry;
import org.onetwo.dbm.mapping.JdbcStatementContext;

/*****
 * 
 * @author wayshall
 *
 */
public class DbmUpdateEventListener extends UpdateEventListener {

	/*****
	 * 不是调用批量接口更新的，取用循环插入的方式，通过调用updateSingleEntity方法来检查是否更新成功！
	 */
	@Override
	protected void doUpdate(DbmUpdateEvent event, JFishMappedEntry entry){
		Object entity = event.getObject();
		DbmEventSource es = event.getEventSource();
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
//			count = this.executeJdbcUpdate(es, entry.makeUpdate(entity));
			if(LangUtils.isMultiple(entity)){
				Collection<Object> entityCol = CUtils.toCollection(entity);
				for(Object e : entityCol){
					throwIfMultiple(entity, e);
					count += updateSingleEntity(false, es, entry, e);
				}
			}else{
				count = this.updateSingleEntity(false, es, entry, entity);
			}
		}
		event.setUpdateCount(count);
	}

	/*********
	 * 更新单个实体，如果更新条数少于1，则表示更新失败，抛出{@link EntityNotFoundException EntityNotFoundException}
	 * @param dymanic
	 * @param es
	 * @param entry
	 * @param singleEntity
	 * @return
	 */
	private int updateSingleEntity(boolean dymanic, DbmEventSource es, JFishMappedEntry entry, Object singleEntity){
		Object currentTransactionVersion = null;

		if(entry.isVersionControll()){
			DbmMappedField versionField = entry.getVersionField();
			JdbcStatementContext<Object[]> versionContext = entry.makeSelectVersion(singleEntity);
			//因为在同一个事务里，实际上得到的version还是旧的，只是防止程序员自己修改version字段
			currentTransactionVersion = es.getDbmJdbcOperations().queryForObject(versionContext.getSql(), versionField.getColumnType(), entry.getId(singleEntity));
			Object entityVersion = entry.getVersionField().getValue(singleEntity);
			
			if(!versionField.getVersionableType().isEquals(entityVersion, currentTransactionVersion)){
				throw new EntityVersionException(entry.getEntityClass(), entry.getId(singleEntity), currentTransactionVersion);
			}
		}
		
		JdbcStatementContext<List<Object[]>> update = dymanic?entry.makeDymanicUpdate(singleEntity):entry.makeUpdate(singleEntity);
		int count = this.executeJdbcUpdate(false, update.getSql(), update.getValue(), es);
		
		if(count<1){
			if(currentTransactionVersion!=null){
//				Object entityVersion = update.getSqlBuilder().getVersionValue(update.getValue().get(0));
//				Object entityVersion = entry.getVersionField().getValue(singleEntity);
				throw new EntityVersionException(entry.getEntityClass(), entry.getId(singleEntity), currentTransactionVersion);
			}else{
				throw new EntityNotFoundException("update count is " + count + ".", singleEntity.getClass(), entry.getId(singleEntity));
			}
		}
		
		this.updateEntityVersionIfNecessary(update.getSqlBuilder(), update.getValue().get(0), singleEntity);
		
		return count;
	}

}
