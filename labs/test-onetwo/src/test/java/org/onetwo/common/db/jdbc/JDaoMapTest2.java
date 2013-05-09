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
public class JDaoMapTest2 {

	private static DatabaseManager dm;
	
	static JDao jdao;
	static Long id;
	static Map<String, Object> user;
	
	@BeforeClass
	public static void setupClass(){
		
		dm = new DefaultDatabaseManage();
		dm.setDbconfigPath("E:/mydev/ejb3/lvyou2/onetwo-common/test/org/onetwo/common/db/jdbc/mysql_test.properties");
		JDBC.init(dm);
		jdao = JDBC.inst().jdao();
	}
	
	@Test
	public void testInsertUserMap2(){
		user = new HashMap<String, Object>();
		user.put(TMeta.table, "t_user");
		user.put(TMeta.pk, "id");
		user.put(TMeta.use_keys_as_fields, true);
		user.put("userName", "userNameMapTest");
		user.put("birthDay", DateUtil.now());
		user.put("email", "username@qq.com");
		user.put("height", 3.3f);
		jdao.save(user);
		id = (Long)user.get("id");
		Assert.assertNotNull(id);
	}
}
