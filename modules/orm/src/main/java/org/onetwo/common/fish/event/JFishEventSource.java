package org.onetwo.common.fish.event;

import org.onetwo.common.fish.spring.JFishDaoImplementor;

public interface JFishEventSource extends JFishDaoImplementor {
	
	/*********
	 * 保存对象和关联属性的对象到数据库，<br/>
	 * 无论是主对象还是关联对象，会根据id是否为null值去判断是保存还是更新。<br/>
	 * <br/>
	 * dymanicIfUpdate为true，更新时只会更新不为null的字段<br/>
	 * dymanicIfUpdate为false，更新时，不管字段是否为null值，都update到数据库；<br/>
	 * <br/>
	 * @param entity
	 * @param dymanicIfUpdate
	 * @param relatedFields
	 * @return
	 */
	public <T> int insertOrUpdate(T entity, boolean dymanicIfUpdate, String... relatedFields);
	
	/*******
	 * 更新实体和指定的关联属性实体<br/>
	 * <br/>
	 * dymanicIfUpdate为true，更新时只会更新不为null的字段<br/>
	 * dymanicIfUpdate为false，更新时，不管字段是否为null值，都update到数据库；<br/>
	 * <br/>
	 * @param entity
	 * @param dymanicUpdate
	 * @param relatedFields
	 * @return
	 */
	public int update(Object entity, boolean dymanicUpdate, String... relatedFields);
	
}
