package org.onetwo.common.dbm.model.service;

import java.util.Date;
import java.util.List;

import org.onetwo.common.dbm.model.entity.UserAutoidEntity;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.core.spi.DbmSession;

public class UserAutoidServiceNoSpringImpl implements UserAutoidService {
	
	/*@Resource
	private UserAutoidDao userAutoidDao;*/

	private DbmSession session;
	
	
	public UserAutoidServiceNoSpringImpl(DbmSession session) {
		super();
		this.session = session;
	}

	public int deleteAll(){
		return this.session.deleteAll(UserAutoidEntity.class);
	}
	
	public int saveList(String userNamePrefix, Date birthday, int count){ 
		List<UserAutoidEntity> list = LangUtils.newArrayList();
		for (int i = 0; i < count; i++) {
			UserAutoidEntity user = new UserAutoidEntity();
			user.setUserName(userNamePrefix+"-batch-"+i);
			user.setPassword("password-batch-"+i);
//			user.setCreateTime(new Date());
			user.setGender(i%2);
			user.setNickName("nickName-batch-"+i);
			user.setEmail("test@qq.com");
			user.setMobile("137"+i);
			user.setBirthday(birthday);
			
			list.add(user);
		}
		int insertCount = session.save(list);
		return insertCount;
	}
	
	public List<UserAutoidEntity> findUserAutoIdEntity(String userName, Date birthday){
		return session.findByProperties(UserAutoidEntity.class, CUtils.asMap(
																"userName:like", userName,
																"birthday", birthday
																));
	}
	
	public int update(List<UserAutoidEntity> users){
		return session.update(users);
	}
	
	public int delete(List<UserAutoidEntity> users){
		return session.delete(users);
	}

}
