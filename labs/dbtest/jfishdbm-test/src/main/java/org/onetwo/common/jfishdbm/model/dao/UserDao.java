package org.onetwo.common.jfishdbm.model.dao;

import java.util.List;

import org.onetwo.common.jfishdbm.model.entity.UserEntity;

public interface UserDao {
	
	public int batchInsert(List<UserEntity> users);

}
