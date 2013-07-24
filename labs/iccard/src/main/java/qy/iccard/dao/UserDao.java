package qy.iccard.dao;

import org.springframework.stereotype.Repository;

import qy.iccard.entity.UserEntity;

@Repository
public class UserDao extends BaseDao<UserEntity, Long> {
	
	public UserDao(){
		super(UserEntity.class);
	}
	
}
