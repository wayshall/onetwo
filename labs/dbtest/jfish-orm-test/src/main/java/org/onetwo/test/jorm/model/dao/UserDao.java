package org.onetwo.test.jorm.model.dao;

import java.util.List;

import org.onetwo.test.jorm.model.entity.UserEntity;

public interface UserDao {
	
	public int batchInsert(List<UserEntity> users);

}
