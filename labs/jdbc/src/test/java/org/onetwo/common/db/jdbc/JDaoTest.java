package org.onetwo.common.db.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onetwo.common.db.wheel.DatabaseManager;
import org.onetwo.common.db.wheel.DefaultDatabaseManage;
import org.onetwo.common.db.wheel.DefaultEntityBuilderFactory;
import org.onetwo.common.db.wheel.DefaultSQLBuilderFactory;
import org.onetwo.common.db.wheel.EntityBuilder;
import org.onetwo.common.db.wheel.EntityBuilderFactory;
import org.onetwo.common.db.wheel.JDBC;
import org.onetwo.common.db.wheel.JDao;
import org.onetwo.common.db.wheel.JPAEntityBuidable;
import org.onetwo.common.db.wheel.TableInfoBuilder;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;

import test.entity.UserEntity;


@SuppressWarnings("unchecked")
public class JDaoTest {

	private static DatabaseManager dm;
//	TableInfo tableInfo;
	static TableInfoBuilder tableInfoBuilder;
	static EntityBuilder entityBuilder;
	
	static EntityBuilderFactory entityBuilderFactory;
//	EntityCallbackFactory entityCallbackFactory;
	static JDao jdao;
	
	@BeforeClass
	public static void setup(){
		dm = new DefaultDatabaseManage();
		
		tableInfoBuilder = new JPAEntityBuidable();
		entityBuilderFactory = new DefaultEntityBuilderFactory(tableInfoBuilder, new DefaultSQLBuilderFactory());
//		entityCallbackFactory = new OracleEntityCallbackFactory(entityBuilderFactory);

//		dm.setDbconfigPath("E:/mydev/ejb3/lvyou2/onetwo-common/test/org/onetwo/common/db/jdbc/mysql_test.properties");
//		jdao = JDao.create(new MySQLWheel(new DatabaseManagerConnectionCreator(dm)));

//		dm.setDbconfigPath("E:/mydev/ejb3/lvyou2/onetwo-common/test/org/onetwo/common/db/jdbc/mysql_test.properties");
//		jdao = JDao.create(new OracleWheel(new DatabaseManagerConnectionCreator(dm)));
		

//		dm.setDbconfigPath("E:/mydev/ejb3/lvyou2/onetwo-common/test/org/onetwo/common/db/jdbc/access_test.properties");
		dm.setDbconfigPath("E:/mydev/ejb3/lvyou2/onetwo-common/test/org/onetwo/common/db/jdbc/mysql_test.properties");
		JDBC.init(dm);
		jdao = JDBC.inst().jdao();
//		jdao = JDao.create(new MySQLWheel(new DatabaseManagerConnectionCreator(dm)));
		
	}
	
//	@Test
	public void testConnection(){
		Assert.assertNotNull(dm.getConnection());
	}

	@Test
	public void testInsertUserSql(){
		entityBuilder = entityBuilderFactory.create(UserEntity.class);
		
		String expected = "insert into T_USER ( CREATE_TIME, AGE, EMAIL, HEIGHT, LAST_UPDATE_TIME, BIRTH_DAY, USER_NAME ) values ( :createTime, :age, :email, :height, :lastUpdateTime, :birthDay, :userName )";
		String sql = entityBuilder.makeStaticInsertSQL();
		LangUtils.println("testInsertUserSql: "+sql);
		Assert.assertEquals(expected.trim(), sql.trim());
	}
	
	@Test
	public void testUpdateUserSql(){
		entityBuilder = entityBuilderFactory.create(UserEntity.class);
		
		String expected = "update T_USER set CREATE_TIME = :createTime, AGE = :age, EMAIL = :email, HEIGHT = :height, LAST_UPDATE_TIME = :lastUpdateTime, BIRTH_DAY = :birthDay, USER_NAME = :userName where id = :id";
		String sql = entityBuilder.makeStaticUpdateSQL();
		LangUtils.println("testUpdateUserSql: "+sql);
		Assert.assertEquals(expected.trim(), sql.trim());
		
	}
	
	@Test
	public void testDeleteUserSql(){
		entityBuilder = entityBuilderFactory.create(UserEntity.class);
		
		String expected = "delete from T_USER where id = :id";
		String sql = entityBuilder.makeStaticDeleteSQL();
		LangUtils.println("testDeleteUserSql: "+sql);
		Assert.assertEquals(expected.trim(), sql.trim());
		
	}

	@Test
	public void testQueryUserSql(){
		entityBuilder = entityBuilderFactory.create(UserEntity.class);
		
		String expected = "select this_.CREATE_TIME, this_.AGE, this_.EMAIL, this_.HEIGHT, this_.id, this_.LAST_UPDATE_TIME, this_.BIRTH_DAY, this_.USER_NAME from T_USER this_ where this_.id = :this_id";
		String sql = entityBuilder.makeStaticQuerySQL();
		LangUtils.println("testQueryUserSql: "+sql);
		Assert.assertEquals(expected.trim(), sql.trim());
		
	}

//	@Test
	public void testUserInsert(){
		
		UserEntity user = new UserEntity();
		user.setUserName("userNameTeset");
		user.setBirthDay(DateUtil.now());
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);
		jdao.save(user);
		LangUtils.debug("insert user Id : ${0} ", user.getId());
		Assert.assertNotNull(user.getId());
//		jdao.delete(workOperate);
	}
//	@Test
	public void testUserInsertOnly(){
		
		UserEntity user = new UserEntity();
		user.setUserName("userNameTeset");
		user.setBirthDay(DateUtil.now());
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);
		jdao.saveOnly(user);
		LangUtils.debug("insert user Id : ${0} ", user.getId());
		Assert.assertNull(user.getId());
//		jdao.delete(workOperate);
	}
	
//	@Test
	public void testBatchInsertUser(){
		
		UserEntity user = null;

		List datas = new ArrayList();
		user = new UserEntity();
		user.setUserName("userNameTeset"+datas.size());
		user.setBirthDay(DateUtil.now());
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);
		datas.add(user);

		user = new UserEntity();
		user.setUserName("userNameTeset"+datas.size());
		user.setBirthDay(DateUtil.now());
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);
		datas.add(user);
		
		jdao.save(datas);
//		jdao.delete(workOperate);
	}
	
//	@Test
	public void testUserUpdate(){
		
		UserEntity user = new UserEntity();
		user.setId(39l);
		user.setUserName("userNameTeset-update"+DateUtil.nowString());
		user.setBirthDay(DateUtil.now());
		user.setHeight(3.3f);

		jdao.update(user);
	}
	
//	@Test
	public void testUserUpdateIgnoreNull(){
		
		UserEntity user = new UserEntity();
		user.setId(36l);
		user.setUserName("userNameTeset-update"+DateUtil.nowString());
		user.setBirthDay(DateUtil.now());
		user.setHeight(3.3f);

		jdao.updateIgnoreNull(user);
	}
	
//	@Test
	public void testDynamicUpdateUser(){
		
		UserEntity user = new UserEntity();
		user.setId(17l);
		user.setUserName("userNameTeset-update"+DateUtil.nowString());
		user.setBirthDay(DateUtil.now());
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);

		jdao.update(user);
	}
	
//	@Test
	public void testQueryUser(){;
		UserEntity user = jdao.find(29l, UserEntity.class);
		LangUtils.println("testQueryUser id[${0}], userName[${1}]", user.getId(), user.getUserName());
		Assert.assertNotNull(user.getId());
		Assert.assertNotNull(user.getUserName());
	}
	
	@Test
	public void testFindByExample(){
		UserEntity user = new UserEntity();
		user.setUserName("userNameTeset");
		user.setEmail("username@qq.com");
		List<UserEntity> list = jdao.findByExample(user);
		LangUtils.debug("testFindByExample datas : ${0}", list);
	}
	
//	@Test
	public void testDeleteUser(){;
		UserEntity user = jdao.delete(13l, UserEntity.class);
		System.out.println("name: " + user.getUserName());
	}
	
//	@Test
	public void testAll(){
		UserEntity user = new UserEntity();
		user.setUserName("userNameTeset");
		user.setBirthDay(DateUtil.date("2012-1-1"));
		user.setEmail("username@qq.com");
		user.setHeight(3.3f);
		jdao.save(user);
		Assert.assertNotNull(user.getId());
		
		UserEntity userQuery = jdao.find(user.getId(), UserEntity.class);
		Assert.assertNotNull(userQuery);
		Assert.assertEquals(user.getUserName(), userQuery.getUserName());

		String str = DateUtil.nowString();
		user.setUserName("userNameTeset-update"+str);
		jdao.update(user);
		userQuery = jdao.find(user.getId(), UserEntity.class);
		Assert.assertEquals(user.getUserName(), userQuery.getUserName());
		
		user = jdao.delete(userQuery.getId(), UserEntity.class);
		userQuery = jdao.find(user.getId(), UserEntity.class);
		Assert.assertNull(userQuery);
	}
}
