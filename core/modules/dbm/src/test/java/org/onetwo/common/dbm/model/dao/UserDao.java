package org.onetwo.common.dbm.model.dao;

import java.util.List;

import org.onetwo.common.dbm.model.entity.UserEntity;

public interface UserDao {
	
	public int batchInsert(List<UserEntity> users);

}
