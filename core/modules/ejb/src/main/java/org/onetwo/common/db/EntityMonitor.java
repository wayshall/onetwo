package org.onetwo.common.db;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@SuppressWarnings("rawtypes")
abstract public class EntityMonitor {
	
	@PrePersist
	abstract public void onPrePersist(Object entity);
	
	@PreUpdate
	abstract public void onPreUpdate(Object entity);
	
	public void timeOnCreate(Object entity, Date time){
		if(!(entity instanceof IBaseEntity))
			return ;
		IBaseEntity baseEntity = (IBaseEntity) entity;
		baseEntity.setCreateTime(time);
		baseEntity.setLastUpdateTime(time);
	}
	
	public void timeOnUpdate(Object entity, Date time){
		if(!(entity instanceof IBaseEntity))
			return ;
		IBaseEntity baseEntity = (IBaseEntity) entity;
		baseEntity.setLastUpdateTime(time);
	}
}
