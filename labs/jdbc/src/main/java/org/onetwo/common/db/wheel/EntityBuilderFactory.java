package org.onetwo.common.db.wheel;


public interface EntityBuilderFactory {
//	public EntityBuilder create(EntityOperation operation, Class<?> entityClass);
	public EntityBuilder create(Object entityClass);
	public EntityBuilder create(TableInfo tableInfo);
}
