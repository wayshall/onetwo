package org.onetwo.common.dbm.model.service;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional
public class TransationalServiceImpl {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private ApplicationContext applicationContext;
	
	public UserAutoidEntity save(){
		UserAutoidEntity user = new UserAutoidEntity();
		user.setUserName("dbm");
		user.setMobile("1333333333");
		user.setEmail("test@test.com");
		user.setStatus(UserStatus.NORMAL);
		
		baseEntityManager.save(user);
		this.applicationContext.publishEvent(new UserSaveEvent(this, user));
		return user;
	}
	
	@TransactionalEventListener
	public void onSave(UserSaveEvent saveEvent){
		System.out.println("saved: " + saveEvent.getUser().getId());
	}
	
	public static class UserSaveEvent {
		final private Object source;
		final private UserAutoidEntity user;
		public UserSaveEvent(Object source, UserAutoidEntity user) {
			super();
			this.source = source;
			this.user = user;
		}
		public Object getSource() {
			return source;
		}
		public UserAutoidEntity getUser() {
			return user;
		}
	}

}
