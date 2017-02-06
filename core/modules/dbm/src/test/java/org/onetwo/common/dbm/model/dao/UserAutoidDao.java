package org.onetwo.common.dbm.model.dao;

import java.util.Date;
import java.util.List;

import org.onetwo.common.db.dquery.annotation.BatchObject;
import org.onetwo.common.db.dquery.annotation.DbmRepository;
import org.onetwo.common.db.dquery.annotation.ExecuteUpdate;
import org.onetwo.common.db.dquery.annotation.Param;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity;

@DbmRepository
public interface UserAutoidDao {
	@ExecuteUpdate
	public int removeByUserName(String userName);
	
	public int batchInsert(List<UserAutoidEntity> users);
	
	public int batchInsert2(@BatchObject List<UserAutoidEntity> users, @Param("allBirthday")Date birthday);
	
	

}
