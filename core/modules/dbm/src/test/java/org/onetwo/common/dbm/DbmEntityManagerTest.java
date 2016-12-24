package org.onetwo.common.dbm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity;
import org.onetwo.common.dbm.model.entity.UserEntity;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity.UserStatus;
import org.onetwo.common.utils.JodatimeUtils;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;

public class DbmEntityManagerTest extends AppBaseTest {

	@Resource
	private BaseEntityManager entityManager;

	private static UserEntity user;

	@Test
	public void testSample(){
		UserAutoidEntity user = new UserAutoidEntity();
		user.setUserName("dbm");
		user.setMobile("1333333333");
		user.setEmail("test@test.com");
		user.setStatus(UserStatus.NORMAL);
		
		//save
		Long userId = entityManager.save(user).getId();
		assertThat(userId, notNullValue());
		
		//update
		String newMobile = "13555555555";
		user.setMobile(newMobile);
		entityManager.update(user);
		
		//fetch by id
		user = entityManager.findById(UserAutoidEntity.class, userId); 
		assertThat(user.getMobile(), is(newMobile));
		
		//find one by properties
		user = entityManager.findOne(UserAutoidEntity.class, 
										"mobile", newMobile,
										"status", UserStatus.NORMAL);
		assertThat(user.getId(), is(userId));
		
		//user querys dsl api
		UserAutoidEntity queryUser = Querys.from(entityManager, UserAutoidEntity.class)
											.where()
												.field("mobile").is(newMobile)
												.field("status").is(UserStatus.NORMAL)
											.end()
											.toQuery()
											.one();
		assertThat(queryUser, is(user));
		
	}

	@Test
	public void testSave() {
		entityManager.removeAll(UserEntity.class);
		user = new UserEntity();
		user.setUserName("JdbcTest");
		user.setBirthday(JodatimeUtils.parse("1982-05-06").toDate());
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);
		user.setAge(28);
		user.setId(10000000000L);
		entityManager.save(user);
		Assert.assertEquals(10000000000L, user.getId(), 0);
		
		UserEntity quser = entityManager.findById(UserEntity.class, user.getId());
		Assert.assertNotNull(quser);
		Assert.assertEquals(user.getId(), quser.getId());
		Assert.assertEquals(user.getUserName(), quser.getUserName());
		
		testUpdate(quser.getId());
		testDelete(quser.getId());
	}

	
	private void testUpdate(Long id){
		UserEntity uuser = new UserEntity();
		uuser.setUserName("test-update-"+user.getUserName());
		uuser.setEmail("test-update-"+user.getEmail());
		uuser.setId(id);
		
		entityManager.save(uuser);
		UserEntity quser = entityManager.findById(UserEntity.class, user.getId());
		Assert.assertNotNull(quser);
		Assert.assertEquals(uuser.getId(), quser.getId());
		Assert.assertEquals(uuser.getUserName(), quser.getUserName());

		Assert.assertEquals(user.getAge(), quser.getAge());
		Assert.assertEquals(user.getBirthday().getTime(), quser.getBirthday().getTime());
	}
	
	private void testDelete(Long id){
		UserEntity duser = entityManager.removeById(UserEntity.class, id);
		Assert.assertNotNull(duser);
		UserEntity quser = entityManager.findById(UserEntity.class, id);
		Assert.assertNull(quser);
	}
	

	@Test
	public void testJFishQuery(){
		entityManager.removeAll(UserEntity.class);
		List<UserEntity> users = LangOps.generateList(20, i->{
			UserEntity user = new UserEntity();
			user.setId(i+1L);
			user.setUserName("JdbcTest");
			user.setBirthday(DateUtils.now());
			user.setEmail("username@qq.com");
			user.setHeight(3.3f);
			user.setAge(28);
			return user;
		});
		entityManager.save(users);
		Page<UserEntity> page = new Page<UserEntity>();
		entityManager.findPage(UserEntity.class, page, "user_name:like", "%Jdbc%");
		Assert.assertEquals(page.getPageSize(), page.getSize());
		for(UserEntity u : page.getResult()){
			LangUtils.println("id: ${0}, name: ${1}", u.getId(), u.getUserName());
		}
	}
	

}
