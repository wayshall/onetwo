package org.onetwo.test.dbtest.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.test.dbtest.model.dao.UserAutoidDao;
import org.onetwo.test.dbtest.model.entity.UserAutoidEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAutoidServiceImpl {
	
	@Resource
	private UserAutoidDao userAutoidDao;
	
	public int saveList(int count){ 
		List<UserAutoidEntity> list = LangUtils.newArrayList();
		for (int i = 0; i < count; i++) {
			UserAutoidEntity user = new UserAutoidEntity();
			user.setUserName("userName-batch-"+i);
			user.setPassword("password-batch-"+i);
//			user.setCreateTime(new Date());
			user.setGender(i%2);
			user.setNickName("nickName-batch-"+i);
			user.setEmail("test@qq.com");
			user.setMobile("137"+i);
			
			list.add(user);
		}
		int insertCount = userAutoidDao.batchInsert(list);
		return insertCount;
	}

}
