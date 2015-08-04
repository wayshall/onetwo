package org.onetwo.common.jfishdbm.model.dao;

import java.util.Date;
import java.util.List;

import org.onetwo.common.db.dquery.annotation.BatchObject;
import org.onetwo.common.db.dquery.annotation.ExecuteUpdate;
import org.onetwo.common.db.dquery.annotation.Name;
import org.onetwo.common.jfishdbm.model.entity.UserAutoidEntity;

public interface UserAutoidDao {
	
	public int batchInsert(List<UserAutoidEntity> users);
	
	public int batchInsert2(@BatchObject List<UserAutoidEntity> users, @Name("allBirthday")Date birthday);
	
	@ExecuteUpdate
	public int removeByUserName(String userName);

}
