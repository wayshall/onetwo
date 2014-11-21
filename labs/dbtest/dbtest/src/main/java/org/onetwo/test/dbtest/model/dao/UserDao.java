package org.onetwo.test.dbtest.model.dao;

import java.util.List;

import org.onetwo.test.dbtest.model.entity.UserEntity;

public interface UserDao {
	
	public int batchInsert(List<UserEntity> users);

}
