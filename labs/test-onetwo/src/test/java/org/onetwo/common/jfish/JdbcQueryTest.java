package org.onetwo.common.jfish;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.QueryBuilder;
import org.onetwo.common.db.QueryBuilderImpl;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.spring.JFishDaoImpl;
import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import test.entity.UserEntity;

@ActiveProfiles({ "jdao", "test" })
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class JdbcQueryTest extends SpringTxJUnitTestCase {

	@Resource
	private JFishDaoImpl jdao;
	
	@Resource
	private BaseEntityManager em;

	private static UserEntity user;


	@Test
	public void testJFishQueryBySQL(){
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
	
	@Test
	public void testJFishQuery(){
		int size = 20;
		List<UserEntity> userlist = em.findByProperties(UserEntity.class, "userName:like", "%Jdbc%", K.MAX_RESULTS, size, K.DEBUG, true);
		Assert.assertEquals(size, userlist.size());
		for(UserEntity u :userlist){
			LangUtils.println("id: ${0}, name: ${1}", u.getId(), u.getUserName());
		}
	}
	
	@Test
	public void testJFishQueryPage(){
		int size = 20;
		Page<UserEntity> page = new Page<UserEntity>();
		em.findPage(UserEntity.class, page, "userName:like", "%Jdbc%", K.MAX_RESULTS, size, K.DEBUG, true);
		Assert.assertTrue(page.getSize()>0);
		Assert.assertEquals(size, page.getSize());
		for(UserEntity u : page.getResult()){
			LangUtils.println("id: ${0}, name: ${1}", u.getId(), u.getUserName());
		}
	}
	
	@Test
	public void testJFishQueryBySQuery(){
		int size = 20;
		Page<UserEntity> page = new Page<UserEntity>();
		
		QueryBuilder q = QueryBuilderImpl.from(UserEntity.class)
						.field("userName").like("%Jdbc%")
						.field("email").isNotNull()
						.desc("id", "birthDay");
		
		em.findPage(UserEntity.class, page, q);
		System.out.println("===============testJFishQueryBySQuery: ");
		Assert.assertTrue(page.getSize()>0);
		Assert.assertEquals(size, page.getSize());
		for(UserEntity u : page.getResult()){
			LangUtils.println("id: ${0}, name: ${1}", u.getId(), u.getUserName());
		}
	}

}
