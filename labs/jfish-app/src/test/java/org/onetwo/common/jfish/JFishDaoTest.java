package org.onetwo.common.jfish;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.spring.JFishDaoImpl;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JFishDaoTest extends JFishBaseNGTest {

	public static final String JFISH_CRUD = "jfishdao.crud";
	public static final String JFISH_QUERY = "jfishdao.query";

//	@Resource
//	private JdbcBaseEntityManager jdbcBaseEntityManager;

	@Resource
	private JFishDaoImpl jdao;

	private static UserEntity user;
	private static Long id;

	private static int insertCount = 100;

	protected UserEntity createUserEntity(int i, String userName){
		UserEntity user = new UserEntity();
		user.setUserName(userName+i);
		user.setBirthDay(DateUtil.now());
		user.setEmail(i+"username@qq.com");
		user.setHeight(3.3f);
		
		return user;
	}
	
	@Test(groups=JFISH_CRUD)
	public void testSave() {
		user = new UserEntity();
		user.setUserName("wayshall");
		user.setBirthDay(DateUtil.now());
		user.setEmail("username@qq.com");
		user.setPassword("123456");
		user.setHeight(3.3f);
		user.setAge(28);
		user.setReadOnlyField("testValue");
		jdao.insert(user);
		System.out.println("id:" + user.getId());
		Assert.assertNotNull(user.getId());
		id = user.getId();
	}


	@Test(dependsOnMethods="testSave", groups=JFISH_CRUD)
	public void testQuery() {
		UserEntity quser = jdao.findById(UserEntity.class, id);
		Assert.assertNotNull(quser);
		Assert.assertEquals(user.getId(), quser.getId());
		Assert.assertEquals(user.getUserName(), quser.getUserName());
		Assert.assertNull(quser.getReadOnlyField());
		
	}

	@Test(dependsOnMethods="testQuery",groups=JFISH_CRUD)
	public void testDynamicUpdate(){
		UserEntity uuser = new UserEntity();
		uuser.setUserName("test-update-"+user.getUserName());
		uuser.setEmail("");
		uuser.setId(id);
		
		jdao.dymanicUpdate(uuser);
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
		Assert.assertNotNull(quser);
		Assert.assertEquals(uuser.getId(), quser.getId());
		Assert.assertEquals(uuser.getUserName(), quser.getUserName());
		Assert.assertEquals(quser.getEmail(), "");

		Assert.assertNotNull(quser.getAge());
		Assert.assertNotNull(quser.getBirthDay());
		Assert.assertTrue(user.getAge().equals(quser.getAge()));
//		System.out.println("user.getBirthDay().getTime() :"+user.getBirthDay().getTime());
//		System.out.println("quser.getBirthDay().getTime():"+quser.getBirthDay().getTime());
		Assert.assertTrue(user.getBirthDay().getTime()/1000==quser.getBirthDay().getTime()/1000);
	}

	@Test(dependsOnMethods="testDynamicUpdate", groups=JFISH_CRUD)
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


	@Test(dependsOnMethods="testUpdate", groups=JFISH_CRUD)
	public void testDelete(){
		int count = jdao.delete(UserEntity.class, id);
		Assert.assertEquals(1, count);
		UserEntity quser = jdao.findById(UserEntity.class, user.getId());
		Assert.assertNull(quser);
	}
	

	@Test(dependsOnMethods="testJFishSaveList", groups=JFISH_QUERY)
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
	}


	@Test(dependsOnGroups=JFISH_CRUD, groups=JFISH_QUERY)
	public void testJFishSaveList(){
		String name = "testJFishSaveList";
		UtilTimerStack.push(name);
		
		List<UserEntity> list = new ArrayList<UserEntity>();
		for(int i=0; i<insertCount; i++){
			UserEntity user = createUserEntity(i, "testJdbcSaveList");
			list.add(user);
		}
		jdao.justInsert(list);
		UtilTimerStack.pop(name);
		
	}

	@Test(dependsOnGroups=JFISH_CRUD, groups=JFISH_QUERY)
	public void testJFishBatchInsert(){
		String name = "testJFishBatchInsert";
		UtilTimerStack.push(name);

		Assert.assertTrue(jdao.batchInsert(new ArrayList())==0);
		
		List<UserEntity> list = new ArrayList<UserEntity>();
		for(int i=0; i<insertCount; i++){
			UserEntity user = createUserEntity(i, "testJFishBatchInsert");
			list.add(user);
		}
		jdao.batchInsert(list);
		UtilTimerStack.pop(name);
		
	}

	@Test(groups=JFISH_QUERY, dependsOnMethods="testJFishSaveList")
	public void testJFishBatchUpdate(){
		String name = "testJFishBatchUpdate";
		UtilTimerStack.push(name);

		Assert.assertTrue(jdao.batchUpdate(new ArrayList())==0);
		
		String sql = "select * from T_USER t where t.user_name like :userName";
		JFishQuery query = this.jdao.createJFishQuery(sql);
		query.setParameter("userName", "%testJFishBatchInsert%");
		query.setResultClass(UserEntity.class);
		List<UserEntity> users = query.getResultList();
		Assert.assertTrue(users.size()>=insertCount);
		
		int index = 0;
		for(UserEntity u : users){
			u.setUserName("testJFishBatchUpdate"+(index++));
		}
		jdao.batchUpdate(users);
		
		query = this.jdao.createJFishQuery(sql);
		query.setParameter("userName", "%testJFishBatchUpdate%");
		users = query.getResultList();
		Assert.assertTrue(users.size()>=insertCount);
		
		UtilTimerStack.pop(name);
		
	}

	@Test(dependsOnMethods="testJFishQuery", groups=JFISH_QUERY)
	public void testJFishDeleteAll(){
		int deleteCount = jdao.deleteAll(UserEntity.class);
		Assert.assertTrue(deleteCount>=insertCount);
	}

}
