package org.onetwo.common.jfish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.example.app.model.member.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;

public class JdbcQueryTest extends JFishBaseNGTest {

	@Resource
	private JFishDaoImplementor jdao;
	
	@Resource
	private BaseEntityManager em;

	private static UserEntity user;
	private static List<UserEntity> uclist;

	@Test
	public void testInsertDatas(){
		uclist = new ArrayList<UserEntity>();
		for(int i=0; i<50; i++){
			UserEntity uc = new UserEntity();
			uc.setUserName("JdbcTest");
			uc.setEmail("way@test.com");
			uc.setBirthDay(new Date());
			uclist.add(uc);
		}
		jdao.insert(uclist);
	}
	

	@Test
	public void testJFishQueryBySQL(){
		String sql = "select t.id, t.user_name, 'test' as status_string from T_USER t where t.user_name like :userName";
		JFishQuery query = this.jdao.createJFishQuery(sql);
		query.setParameter("userName", "%Jdbc%");
		int size = 20;
		List<UserEntity> userlist = query.setResultClass(UserEntity.class).setFirstResult(0).setMaxResults(size).getResultList();
		Assert.assertEquals(size, userlist.size());
		for(UserEntity u :userlist){
			Assert.assertEquals("test", u.getStatusString());
			LangUtils.println("id: ${0}, name: ${1}, statusString: ${2}", u.getId(), u.getUserName(), u.getStatusString());
		}
	}
	
	@Test
	public void testJFishQuery(){
		UtilTimerStack.setActive(true);
		JFishQueryValue queryValue = JFishQueryValue.create("select * from T_USER t where t.user_name like ? and t.id > ? ");
		queryValue.setValue(1, 1l);
		queryValue.setValue(0, "%Jdbc%");
		List<UserEntity> userlist = jdao.findList(queryValue);
		Assert.assertTrue(userlist.size()>1);
	}
	
	@Test
	public void testFindByProperties(){
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
	public void testDeleteDatas(){
		jdao.delete(uclist);
	}
}
