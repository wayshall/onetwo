package org.onetwo.common.jfishdbm;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.jackson.UserEntity;
import org.onetwo.common.spring.test.SpringBaseJUnitTestCase;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ActiveProfiles({ "jdao", "test" })
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class JFishEntityManagerTest extends SpringBaseJUnitTestCase {

//	@Resource
//	private JdbcBaseEntityManager jdbcBaseEntityManager;

	@Resource
	private BaseEntityManager em;

	private static UserEntity user;
	private static Long id;

	@Test
	public void testSave() {
		user = new UserEntity();
		user.setUserName("JdbcTest");
		user.setBirthDay(DateUtil.now());
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);
		user.setAge(28);
		em.save(user);
		System.out.println("id:" + user.getId());
		Assert.assertNotNull(user.getId());
		id = user.getId();
	}

	@Test
	public void testQuery() {
		UserEntity quser = em.findById(UserEntity.class, id);
		Assert.assertNotNull(quser);
		Assert.assertEquals(user.getId(), quser.getId());
		Assert.assertEquals(user.getUserName(), quser.getUserName());
	}
	
	
	@Test
	public void testUpdate(){
		UserEntity uuser = new UserEntity();
		uuser.setUserName("test-update-"+user.getUserName());
		uuser.setEmail("test-update-"+user.getEmail());
		uuser.setId(id);
		
		em.save(uuser);
		UserEntity quser = em.findById(UserEntity.class, user.getId());
		Assert.assertNotNull(quser);
		Assert.assertEquals(uuser.getId(), quser.getId());
		Assert.assertEquals(uuser.getUserName(), quser.getUserName());

		Assert.assertNull(quser.getAge());
		Assert.assertNull(quser.getBirthDay());
		Assert.assertFalse(user.getAge().equals(quser.getAge()));
		Assert.assertFalse(user.getBirthDay().equals(quser.getBirthDay()));
	}
	
	@Test
	public void testDelete(){
		UserEntity duser = em.removeById(UserEntity.class, id);
		Assert.assertNotNull(duser);
		UserEntity quser = em.findById(UserEntity.class, user.getId());
		Assert.assertNull(quser);
	}
	

	@Test
	public void testJFishQuery(){
		Page<UserEntity> page = new Page<UserEntity>();
		em.findPage(UserEntity.class, page, "user_name:like", "%Jdbc%");
		Assert.assertEquals(page.getPageSize(), page.getSize());
		for(UserEntity u : page.getResult()){
			LangUtils.println("id: ${0}, name: ${1}", u.getId(), u.getUserName());
		}
	}
	

}
