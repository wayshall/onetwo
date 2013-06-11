package org.example.app.model.member;

import org.example.app.model.member.dao.UserDao;
import org.example.app.model.member.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.jfish.JFishBaseJUnitTest;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends JFishBaseJUnitTest {
	
	@Autowired
	private UserDao userDao;
	
	private int insertCount = 10;
	
	@Test
	public void testSave(){
		for (int j = 1; j < insertCount; j++) {
			UserEntity user = this.createUserEntity(j, "test");
			user.setId(Long.valueOf(j));
			int rs = userDao.save(user).executeUpdate();
			Assert.assertEquals(1, rs);
		}
	}
	
	@Test
	public void query(){
		UserEntity user = userDao.queryById(1L);
		Assert.assertNotNull(user);

		user = userDao.queryByUserName("test1"); 
		Assert.assertNotNull(user);
		
		UserEntity quser = userDao.createUserNameQuery("test1").setResultClass(UserEntity.class).getSingleResult(); 
		Assert.assertNotNull(quser);
		Assert.assertEquals(user.getUserName(), quser.getUserName());
	}

}
