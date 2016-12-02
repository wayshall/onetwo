package org.onetwo.common.dbm.model.service;

import java.util.Date;
import java.util.List;

import org.onetwo.common.dbm.model.entity.UserAutoidEntity;

public interface UserAutoidService {

	public int deleteAll();

	public int saveList(String userNamePrefix, Date birthday, int count);

	public List<UserAutoidEntity> findUserAutoIdEntity(String userName,
			Date birthday);

	public int update(List<UserAutoidEntity> users);

	public int delete(List<UserAutoidEntity> users);

}