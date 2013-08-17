package org.example.model.member.service.impl;
import org.springframework.stereotype.Service;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import org.example.model.member.entity.UserEntity;

@Transactional
@Service
public class UserServiceImpl extends HibernateCrudServiceImpl<UserEntity, Long> {

	public void forTest(){
		
		BaseEntityManager entityManager = getBaseEntityManager();
		
		UserEntity user = new UserEntity();
		user.setUserName("test");
		entityManager.save(user);
		
	}
}