package org.example.app.model.member;

import java.util.List;

import org.example.app.model.member.dao.UserDao;
import org.example.app.model.member.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.jfish.JFishBaseJUnitTest;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoDqTest extends JFishBaseJUnitTest {
	
	@Autowired
	private UserDao userDao;
	
	private int insertCount = 10;
	
	@Test
	public void testAll(){
		testSave();
		testQuery();
		testDelete();
	}

//	@Test
	public void testSave(){
//		testDelete();
		for (int j = 1; j <= insertCount; j++) {
			UserEntity user = this.createUserEntity(j, "test");
			user.setId(Long.valueOf(j));
			int rs = userDao.save(user).executeUpdate();
			Assert.assertEquals(1, rs);
		}
	}
	
//	@Test
	public void testQuery(){
		
		UserEntity user = userDao.queryById(1L);
		Assert.assertNotNull(user);

		user = userDao.queryByUserName("test1"); 
		Assert.assertNotNull(user);
		
		UserEntity quser = userDao.createUserNameQuery("test1").setResultClass(UserEntity.class).getSingleResult(); 
		Assert.assertNotNull(quser);
		Assert.assertEquals(user.getUserName(), quser.getUserName());
		
		List<UserEntity> users = userDao.queryListByUserName("test%");
		Assert.assertEquals(insertCount, users.size());
		
		Page<UserEntity> page = userDao.queryPageByUserName(new Page<UserEntity>(), "test%");
		Assert.assertEquals(insertCount, page.getResult().size());
	}
	
//	@Test
	public void testDelete(){
		int deleteCount = userDao.deleteByUserName("test%").executeUpdate();
		Assert.assertEquals(insertCount, deleteCount);
	}

}
