package org.onetwo.common.jfish;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.example.app.model.member.entity.RoleEntity;
import org.example.app.model.member.entity.UserEntity;
import org.example.app.model.member.entity.UserRoleEntity;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

@TransactionConfiguration(defaultRollback = false)
@Test
public class JFishEntityManagerTest extends JFishBaseNGTest {
	public static final String JFISH_EM_CRUD = "jfish.em.crud";
	public static final String JFISH_EM_QUERY = "jfish.em.query";

//	@Resource
//	private JdbcBaseEntityManager jdbcBaseEntityManager;

	@Resource
	private JFishEntityManager em;

	private static UserEntity user;
	private static RoleEntity role;
	private static UserRoleEntity ur;
	private static Long id;
	
	private static int INSERT_FOR_QUERY_TEST = 20;
	
	@Test(groups=JFISH_EM_CRUD)
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
	

	@Test(groups=JFISH_EM_CRUD)
	public void testSaveRole() {
		role = this.em.findUnique(RoleEntity.class, "name", "admin");
		if(role==null){
			role = new RoleEntity();
			role.setName("admin");
			em.save(role);
		}
		Assert.assertNotNull(role.getId());
	}

	@Test(dependsOnMethods={"testSave", "testSaveRole"}, groups=JFISH_EM_CRUD)
	public void testUserRoleSave() {
		ur = new UserRoleEntity();
		ur.setUserId(user.getId());
		ur.setRoleId(role.getId());
		UserRoleEntity ur1 = em.save(ur);
		UserRoleEntity ur2 = em.save(ur);
		Assert.assertEquals(ur1.getUserId(), ur2.getUserId());
		Assert.assertEquals(ur1.getRoleId(), ur2.getRoleId());
	}

	@Test(dependsOnMethods="testUserRoleSave", groups=JFISH_EM_CRUD)
	public void testUserRoleDelete() {
//		UserRoleEntity aur = em.findUnique(UserRoleEntity.class, "userId", ur.getUserId(), "roleId", ur.getRoleId());
		em.remove(ur);
	}

	@Test(dependsOnMethods="testSave", groups=JFISH_EM_CRUD)
	public void testFind() {
		UserEntity quser = em.findById(UserEntity.class, id);
		Assert.assertNotNull(quser);
		Assert.assertEquals(user.getId(), quser.getId());
		Assert.assertEquals(user.getUserName(), quser.getUserName());
	}
	

	@Test(dependsOnMethods="testSave", groups=JFISH_EM_CRUD)
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
	}

	@Test(dependsOnMethods="testUpdate", groups=JFISH_EM_CRUD)
	public void testDelete(){
		UserEntity duser = em.removeById(UserEntity.class, id);
		Assert.assertNotNull(duser);
		UserEntity quser = em.findById(UserEntity.class, user.getId());
		Assert.assertNull(quser);
	}

	@Test(dependsOnGroups=JFISH_EM_CRUD, groups=JFISH_EM_QUERY)
	public void testInsertForQueryTest(){
		List<UserEntity> list = new ArrayList<UserEntity>();
		for(int i=0; i<INSERT_FOR_QUERY_TEST; i++){
			UserEntity user = createUserEntity(i, "Jdbc");
			list.add(user);
		}
		em.getJfishDao().justInsert(list);
	}
	

	@Test(dependsOnMethods="testInsertForQueryTest", groups=JFISH_EM_QUERY)
	public void testJFishQuery(){
		Page<UserEntity> page = new Page<UserEntity>();
		page.setPageSize(INSERT_FOR_QUERY_TEST);
		em.findPage(UserEntity.class, page, "user_name:like", "%Jdbc%");
		Assert.assertEquals(page.getPageSize(), page.getSize());
		for(UserEntity u : page.getResult()){
			LangUtils.println("id: ${0}, name: ${1}", u.getId(), u.getUserName());
		}
	}


	@Test(dependsOnMethods="testJFishQuery", groups=JFISH_EM_QUERY)
	public void testCreateSqlQuery(){
		DataQuery dg = em.createSQLQuery("select t.id, t.user_name, 'test' as status_string from T_USER t where t.user_name like :userName", UserEntity.class);
		dg.setParameter("userName", "%Jdbc%");
		dg.setMaxResults(INSERT_FOR_QUERY_TEST);
		List datalist = dg.getResultList();
		Assert.assertEquals(INSERT_FOR_QUERY_TEST, datalist.size());
	}
	

	@Test(dependsOnMethods="testCreateSqlQuery", groups=JFISH_EM_QUERY)
	public void testInsertFromSQL(){
		long id = 111111l;
		JFishQuery jq = em.createJFishQueryByQName("TUser.insert", "id", id, "userName", "testUserName");
		int rs = jq.executeUpdate();
		Assert.assertEquals(rs, 1);
	}

	@Test(dependsOnMethods="testInsertFromSQL", groups=JFISH_EM_QUERY)
	public void testClearForTest(){
		int deleteCount = em.removeAll(UserEntity.class);
		System.out.println("testClearForTest deleteCount：" + deleteCount);
		Assert.assertTrue(deleteCount>=INSERT_FOR_QUERY_TEST);
	}
	
}
