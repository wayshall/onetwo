package org.onetwo.common.dbm;

import java.util.Date;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.db.BaseCrudEntityManager;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity.UserStatus;
import org.onetwo.common.profiling.TimeCounter;

public class BaseCrudEntityManagerTest extends AppBaseTest {
	
	private BaseCrudEntityManager<UserAutoidEntity, Long> userDao = BaseCrudEntityManager.createCrudManager(UserAutoidEntity.class);


	private UserAutoidEntity createUserAutoidEntity(int i){
		String userNamePrefix = "BaseCrudEntityManager";;
		UserAutoidEntity user = new UserAutoidEntity();
		user.setUserName(userNamePrefix+"-insert-"+i);
		user.setPassword("password-insert-"+i);
		user.setGender(i%2);
		user.setNickName("nickName-insert-"+i);
		user.setEmail("test@qq.com");
		user.setMobile("137"+i);
		user.setBirthday(new Date());
		user.setStatus(UserStatus.NORMAL);
		return user;
	}
	
	@Test
	public void testInsert(){
		int insertCount = 10;
		//精确到秒，否则会有误差，比如2015-05-06 13:49:09.783存储到mysql后会变成2015-05-06 13:49:10，mysql的datetime只能精确到秒
		TimeCounter t = new TimeCounter("testInsert").logger((logSource, msg, args)->{
			String s = String.format(msg, args);
			System.out.println(s);
		});
		t.start();
		String userName = "unique_user_name_";
		Stream.iterate(1, item->item+1).limit(insertCount).forEach(item->{
			UserAutoidEntity entity = createUserAutoidEntity(item);
			entity.setUserName(userName);
			userDao.save(entity);
		});
		t.stop();
		
		UserAutoidEntity user = userDao.findOne("userName", userName);
		Assert.assertEquals(UserStatus.NORMAL, user.getStatus());
		
//		int count = userDao.removeAll();
	}
}
