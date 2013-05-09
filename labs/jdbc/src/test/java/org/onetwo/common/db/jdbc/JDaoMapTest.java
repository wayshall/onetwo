package org.onetwo.common.db.jdbc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onetwo.common.db.wheel.DatabaseManager;
import org.onetwo.common.db.wheel.DefaultDatabaseManage;
import org.onetwo.common.db.wheel.EntityTableInfoBuilder.TMeta;
import org.onetwo.common.db.wheel.JDBC;
import org.onetwo.common.db.wheel.JDao;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;

import test.entity.UserEntity;

@SuppressWarnings("unchecked")
public class JDaoMapTest {

	private static DatabaseManager dm;
	
	static JDao jdao;
	static Long id;
	static Map<String, Object> userMeta;
	static Map<String, Object> user;
	
	@BeforeClass
	public static void setupClass(){
		userMeta = new HashMap<String, Object>();
		userMeta.put(TMeta.table, "t_user");
		userMeta.put(TMeta.pk, "id");
		userMeta.put(":id", Long.class);
		userMeta.put(":userName", String.class);
		userMeta.put(":email", String.class);
		userMeta.put(":height", Float.class);
		userMeta.put(":birthDay", Date.class);
		
		dm = new DefaultDatabaseManage();
		dm.setDbconfigPath("E:/mydev/ejb3/lvyou2/onetwo-common/test/org/onetwo/common/db/jdbc/mysql_test.properties");
		JDBC.init(dm);
		jdao = JDBC.inst().jdao();
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testUserMapByExample(){
		Map<String, Object> query = new HashMap<String, Object>();
		query.put(TMeta.entity_meta, userMeta);
		query.put("userName", "userNameTeset");
		List<Map<String, Object>> list = jdao.findByExample(query);
		for(Map<String, Object> map : list){
			LangUtils.debug("testUserMapByExample : ${0}", map);
		}
	}
	
	@Test
	public void testInsertUserMap(){
		user = new HashMap<String, Object>();
		user.put(TMeta.entity_meta, userMeta);
		user.put("userName", "userNameMapTest");
		user.put("birthDay", DateUtil.now());
		user.put("email", "username@qq.com");
		user.put("height", 3.3f);
		jdao.save(user);
		id = (Long)user.get("id");
		Assert.assertNotNull(id);
	}
	
	@Test
	public void testUserMapUpdate(){
		
		Map<String, Object> u = new HashMap<String, Object>();
		u.put(TMeta.entity_meta, userMeta);
		
		u.put("userName", "update_userNameMapTeset");
		u.put("birthDay", DateUtil.now());
		u.put("email", "username@qq.com");
		u.put("height", 3.4f);
		u.put("age", 11);
		u.put("id", id);
		jdao.update(u);

		Map map = jdao.find(id, userMeta);
		Assert.assertEquals(u.get("userName"), map.get("userName"));
		
		user = u;
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testUserMapQuery(){
		Map map = jdao.find(id, userMeta);
		LangUtils.debug("testFindUserMap : ${0}", map);
		Assert.assertEquals(user.get("userName"), map.get("userName"));
	}
	
	@Test
	public void testUserMapDelete(){
		Map map = jdao.delete(id, userMeta);
		LangUtils.debug("testDeleteUserMap : ${0}", map);
	}
	
//	@Test
	public void testUserDeleteOnly(){
		Map<String, Object> user = new HashMap<String, Object>();
		user.put(":table", "t_user");
		user.put(TMeta.pk, "id");
		jdao.deleteOnly(30l, UserEntity.class);
	}
}
