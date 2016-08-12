package org.onetwo.common.jfishdbm;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.DateUtil;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.jfishdbm.model.entity.UserEntity;
import org.onetwo.common.utils.JodatimeUtils;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;

//@TransactionConfiguration(defaultRollback = false)
public class JFishEntityManagerTest extends AppBaseTest {

//	@Resource
//	private JdbcBaseEntityManager jdbcBaseEntityManager;

	@Resource
	private BaseEntityManager em;

	private static UserEntity user;

	@Test
	public void testSave() {
		em.removeAll(UserEntity.class);
		user = new UserEntity();
		user.setUserName("JdbcTest");
		user.setBirthday(JodatimeUtils.parse("1982-05-06").toDate());
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);
		user.setAge(28);
		user.setId(10000000000L);
		em.save(user);
		Assert.assertEquals(10000000000L, user.getId(), 0);
		
		UserEntity quser = em.findById(UserEntity.class, user.getId());
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
		
		em.save(uuser);
		UserEntity quser = em.findById(UserEntity.class, user.getId());
		Assert.assertNotNull(quser);
		Assert.assertEquals(uuser.getId(), quser.getId());
		Assert.assertEquals(uuser.getUserName(), quser.getUserName());

		Assert.assertEquals(user.getAge(), quser.getAge());
		Assert.assertEquals(user.getBirthday().getTime(), quser.getBirthday().getTime());
	}
	
	private void testDelete(Long id){
		UserEntity duser = em.removeById(UserEntity.class, id);
		Assert.assertNotNull(duser);
		UserEntity quser = em.findById(UserEntity.class, id);
		Assert.assertNull(quser);
	}
	

	@Test
	public void testJFishQuery(){
		em.removeAll(UserEntity.class);
		List<UserEntity> users = LangOps.generateList(20, i->{
			UserEntity user = new UserEntity();
			user.setId(i+1L);
			user.setUserName("JdbcTest");
			user.setBirthday(DateUtil.now());
			user.setEmail("username@qq.com");
			user.setHeight(3.3f);
			user.setAge(28);
			return user;
		});
		em.save(users);
		Page<UserEntity> page = new Page<UserEntity>();
		em.findPage(UserEntity.class, page, "user_name:like", "%Jdbc%");
		Assert.assertEquals(page.getPageSize(), page.getSize());
		for(UserEntity u : page.getResult()){
			LangUtils.println("id: ${0}, name: ${1}", u.getId(), u.getUserName());
		}
	}
	

}
