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
public class JDaoQueryTest {

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

	@Test
	public void testQuery(){
		Map<Object, Object> query = new HashMap<Object, Object>();
		query.put("user_Name:like", "userName");
		List<Map<String, Object>> list = jdao.findByProperties(userMeta, query);
		for(Map<String, Object> map : list){
			LangUtils.debug("testUserMapByExample : ${0}", map);
		}
	}
	
}
