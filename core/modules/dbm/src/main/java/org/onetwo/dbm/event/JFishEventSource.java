package org.onetwo.dbm.event;

import java.util.Map;

import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.dbm.support.DbmDaoImplementor;

public interface JFishEventSource extends DbmDaoImplementor {
	
	/*********
	 * 保存对象和关联属性的对象到数据库，<br/>
	 * 无论是主对象还是关联对象，会根据id是否为null值去判断是保存还是更新。<br/>
	 * <br/>
	 * dymanicIfUpdate为true，更新时只会更新不为null的字段<br/>
	 * dymanicIfUpdate为false，更新时，不管字段是否为null值，都update到数据库；<br/>
	 * <br/>
	 * @param entity
	 * @param dymanicIfUpdate
	 * @return
	 */
	public <T> int insertOrUpdate(T entity, boolean dymanicIfUpdate);
	
	/*******
	 * 更新实体和指定的关联属性实体<br/>
	 * <br/>
	 * dymanicIfUpdate为true，更新时只会更新不为null的字段<br/>
	 * dymanicIfUpdate为false，更新时，不管字段是否为null值，都update到数据库；<br/>
	 * <br/>
	 * @param entity
	 * @param dymanicUpdate
	 * @return
	 */
	public int update(Object entity, boolean dymanicUpdate);
	
	public SelectExtQuery createExtQuery(Class<?> entityClass, Map<Object, Object> properties);
	
}
