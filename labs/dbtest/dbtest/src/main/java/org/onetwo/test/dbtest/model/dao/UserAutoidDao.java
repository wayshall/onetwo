package org.onetwo.test.dbtest.model.dao;

import java.util.List;

import org.onetwo.test.dbtest.model.entity.UserAutoidEntity;

public interface UserAutoidDao {
	
	public int batchInsert(List<UserAutoidEntity> users);

}
