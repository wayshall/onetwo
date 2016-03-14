package org.onetwo.common.jfishdbm.model.dao;

import java.util.List;

import org.onetwo.common.db.dquery.annotation.Dispatcher;
import org.onetwo.common.jfishdbm.model.entity.UserAutoidEntity;
import org.onetwo.common.utils.Page;

public interface UserAutoidDao2 {

	public List<UserAutoidEntity> findUserList(@Dispatcher String status);
	public void findUserPage(Page<UserAutoidEntity> page, String userName);

}
