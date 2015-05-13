package org.onetwo.common.jfishdbm.model.dao;

import java.util.Date;
import java.util.List;

import org.onetwo.common.jfishdbm.model.entity.UserAutoidEntity;
import org.onetwo.plugins.dq.annotations.BatchObject;
import org.onetwo.plugins.dq.annotations.ExecuteUpdate;
import org.onetwo.plugins.dq.annotations.Name;

public interface UserAutoidDao {
	
	public int batchInsert(List<UserAutoidEntity> users);
	
	public int batchInsert2(@BatchObject List<UserAutoidEntity> users, @Name("allBirthday")Date birthday);
	
	@ExecuteUpdate
	public int removeByUserName(String userName);

}
