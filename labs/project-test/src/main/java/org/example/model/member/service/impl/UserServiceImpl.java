package org.example.model.member.service.impl;
import org.example.model.member.entity.UserEntity;
import org.example.utils.ErrorCodes;
import org.example.utils.ErrorCodes.CustomerServiceErrors;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.fish.exception.JFishServiceException;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl extends HibernateCrudServiceImpl<UserEntity, Long> {

	public void forTest(){
		
		BaseEntityManager entityManager = getBaseEntityManager();
		
		UserEntity user = new UserEntity();
		user.setUserName("test");
		entityManager.save(user);
		
		boolean error = true;
		if(error){
			throw new JFishServiceException(CustomerServiceErrors.ALREADY_REPORT_LOSS);
		}
		
	}
}