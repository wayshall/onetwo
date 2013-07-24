package org.onetwo.common.hibernate;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.onetwo.common.db.IBaseEntity;

public class AutoUpdateTimeMonitor {
	
//	protected Logger logger = Logger.getLogger(this.getClass());
	
	public AutoUpdateTimeMonitor(){
	}

	@PrePersist
	public void onPrePersist(Object entity) {
		if(entity==null)
			return ;
		Date now = new Date();
		timeOnCreate(entity, now);
	}

	@PreUpdate
	public void onPreUpdate(Object entity) {
		if(entity==null)
			return ;
		Date now = new Date();
		timeOnUpdate(entity, now);
	}
	
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
