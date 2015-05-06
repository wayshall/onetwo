package org.onetwo.common.jfish;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.jfishdb.JFishQuery;
import org.onetwo.common.jfishdb.spring.JFishDaoImpl;
import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import test.entity.UserEntity;

@ActiveProfiles({ "jdao", "test" })
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class JdbcTest extends SpringTxJUnitTestCase {

//	@Resource
//	private JdbcBaseEntityManager jdbcBaseEntityManager;

	@Resource
	private JFishDaoImpl jdao;

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
		jdao.insert(user);
		System.out.println("id:" + user.getId());
		Assert.assertNotNull(user.getId());
		id = user.getId();
	}

	@Test
	public void testQuery() {
		UserEntity quser = jdao.findById(UserEntity.class, id);
		Assert.assertNotNull(quser);
		Assert.assertEquals(user.getId(), quser.getId());
		Assert.assertEquals(user.getUserName(), quser.getUserName());
	}
	
	@Test
	public void testDynamicUpdate(){
		UserEntity uuser = new UserEntity();
		uuser.setUserName("test-update-"+user.getUserName());
		uuser.setEmail("test-update-"+user.getEmail());
		uuser.setId(id);
		
		jdao.dymanicUpdate(uuser);
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
		Assert.assertNotNull(quser);
		Assert.assertEquals(uuser.getId(), quser.getId());
		Assert.assertEquals(uuser.getUserName(), quser.getUserName());

		Assert.assertNotNull(quser.getAge());
		Assert.assertNotNull(quser.getBirthDay());
		Assert.assertTrue(user.getAge().equals(quser.getAge()));
//		System.out.println("user.getBirthDay().getTime() :"+user.getBirthDay().getTime());
//		System.out.println("quser.getBirthDay().getTime():"+quser.getBirthDay().getTime());
		Assert.assertTrue(user.getBirthDay().getTime()/1000==quser.getBirthDay().getTime()/1000);
	}
	
	@Test
	public void testUpdate(){
		UserEntity uuser = new UserEntity();
		uuser.setUserName("test-update-"+user.getUserName());
		uuser.setEmail("test-update-"+user.getEmail());
		uuser.setId(id);
		
		jdao.update(uuser);
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
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
		int count = jdao.delete(UserEntity.class, id);
		Assert.assertEquals(1, count);
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
		Assert.assertNull(quser);
	}
	
	@Test
	public void testSaveList(){
		int insertCount = 10;
		List<UserEntity> list = new ArrayList<UserEntity>();
		for(int i=0; i<insertCount; i++){
			UserEntity user = new UserEntity();
			user.setUserName("JdbcTest"+i);
			user.setBirthDay(DateUtil.now());
			user.setEmail(i+"username@qq.com");
			user.setHeight(3.3f);
			list.add(user);
//			System.out.println("id:"+user.getId());
//			Assert.assertNotNull(user.getId());
		}
		int total = jdao.batchInsert(list);
		Assert.assertTrue(insertCount==total);
		
	}
	

//	@Test
	public void testJFishQuery(){
		String sql = "select * from T_USER t where t.user_name like :userName";
		JFishQuery query = this.jdao.createJFishQuery(sql);
		query.setParameter("userName", "%Jdbc%");
		int size = 20;
		List<UserEntity> userlist = query.setResultClass(UserEntity.class).setFirstResult(0).setMaxResults(size).getResultList();
		Assert.assertEquals(userlist.size(), size);
		for(UserEntity u :userlist){
			LangUtils.println("id: ${0}, name: ${1}", u.getId(), u.getUserName());
		}
		int deleteTotal = jdao.delete(userlist);
		Assert.assertEquals(size, deleteTotal);
	}
	

}
