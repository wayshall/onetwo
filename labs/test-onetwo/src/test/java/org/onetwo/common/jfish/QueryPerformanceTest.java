package org.onetwo.common.jfish;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nutz.dao.Cnd;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.QueryBuilder;
import org.onetwo.common.db.QueryBuilderImpl;
import org.onetwo.common.fish.spring.JFishDaoImpl;
import org.onetwo.common.nutz.NutzBaseDao;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.onetwo.common.utils.LangUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import test.entity.UserEntity;

@ActiveProfiles({"test"})
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback=false)
public class QueryPerformanceTest extends SpringTxJUnitTestCase {
	
	@Resource
	private JFishDaoImpl jdao;
	
	@Resource
	private BaseEntityManager jFishEntityManagerForTest;

	@Resource
	private DataSource dataSource;
	private NutzBaseDao nutzDao;
	
	private int insertCount = 10000;
	

	private int size = 20;
	
	@Before
	public void setup(){
		nutzDao = new NutzBaseDao(dataSource);
		UtilTimerStack.setActive(true);
		String pname = "test";
		UtilTimerStack.push(pname);
		UtilTimerStack.pop(pname);

	}
	
	@Test
	public void testQuery(){
		this.testNutzQuery();
		this.testJFishQuery();
		this.testJFishNamedQuery();
		this.testJFishSquery();
		this.testJFishEntityManager();
		System.out.println("---------------------------------------------------------------------");
		
		this.testNutzQuery();
		this.testJFishQuery();
		this.testJFishNamedQuery();
		this.testJFishSquery();
		this.testJFishEntityManager();
		System.out.println("---------------------------------------------------------------------");
		
		this.testNutzQuery();
		this.testJFishQuery();
		this.testJFishNamedQuery();
		this.testJFishSquery();
		this.testJFishEntityManager();
		System.out.println("---------------------------------------------------------------------");
		
		this.testNutzQuery();
		this.testJFishQuery();
		this.testJFishNamedQuery();
		this.testJFishSquery();
		this.testJFishEntityManager();
	}
	
	public void testJFishQuery(){
		String name = "testJFishQuery";
		UtilTimerStack.push(name);
		
		List<UserEntity> userlist = jdao.findList("select * from T_USER where user_name like ? order by id desc limit 0, "+size, new Object[]{"%jdbc%"}, UserEntity.class);
		Assert.assertEquals(20, userlist.size());
		
		UtilTimerStack.pop(name);
	}
	
	public void testJFishNamedQuery(){
		String name = "testJFishNamedQuery";
		UtilTimerStack.push(name);
		
		List<UserEntity> userlist = jdao.findList("select * from T_USER where user_name like :userName order by id desc limit 0, "+size, LangUtils.asMap("userName", "%jdbc%"), UserEntity.class);
		Assert.assertEquals(20, userlist.size());
		
		UtilTimerStack.pop(name);
	}
	
	public void testJFishEntityManager(){
		String name = "testJFishEntityManager";
		UtilTimerStack.push(name);
		
		List<UserEntity> userlist = this.jFishEntityManagerForTest.findByProperties(UserEntity.class, "user_name:like", "%jdbc%", K.MAX_RESULTS, size, K.DEBUG, true);
		Assert.assertEquals(20, userlist.size());
		
		UtilTimerStack.pop(name);
	}
	
	public void testJFishSquery(){
		String name = "testJFishSquery";
		UtilTimerStack.push(name);
		
		QueryBuilder sq = QueryBuilderImpl.where().debug()
				.field("userName").like("%jdbc%")
				.limit(0, size);
		List<UserEntity> userlist = this.jFishEntityManagerForTest.findByProperties(UserEntity.class, sq);
		Assert.assertEquals(20, userlist.size());
		
		UtilTimerStack.pop(name);
	}
	


	public void testNutzQuery(){
		String name = "testNutzQuery";
		UtilTimerStack.push(name);
		List<UserEntity> userlist = this.nutzDao.query(UserEntity.class, Cnd.wrap("user_name like '%jdbc%' order by id desc limit 0, "+size));
		Assert.assertEquals(20, userlist.size());
		UtilTimerStack.pop(name);
	}
	
}
