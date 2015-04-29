package org.onetwo.test.jorm.model.dao;

import java.util.List;

import org.onetwo.test.jorm.model.entity.UserAutoidEntity;

public interface UserAutoidDao {
	
	public int batchInsert(List<UserAutoidEntity> users);

}
