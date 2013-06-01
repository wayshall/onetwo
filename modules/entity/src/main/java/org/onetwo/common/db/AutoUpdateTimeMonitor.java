package org.onetwo.common.db;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AutoUpdateTimeMonitor extends EntityMonitor {
	
//	protected Logger logger = Logger.getLogger(this.getClass());
	
	public AutoUpdateTimeMonitor(){
	}

	@PrePersist
	@Override
	public void onPrePersist(Object entity) {
		if(entity==null)
			return ;
		Date now = new Date();
		timeOnCreate(entity, now);
	}

	@PreUpdate
	@Override
	public void onPreUpdate(Object entity) {
		if(entity==null)
			return ;
		Date now = new Date();
		timeOnUpdate(entity, now);
	}

}
