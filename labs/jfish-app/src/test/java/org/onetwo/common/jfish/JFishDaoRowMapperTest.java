package org.onetwo.common.jfish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.example.app.model.member.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.fish.spring.JFishDaoImpl;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;

public class JFishDaoRowMapperTest extends JFishBaseJUnitTest {

	@Resource
	private JFishDaoImpl jdao;

	private static int insertCount = 10;

	protected UserEntity createUserEntity(int i, String userName){
		UserEntity user = new UserEntity();
		user.setUserName(userName+i);
		user.setBirthDay(DateUtil.now());
		user.setEmail(i+"username@qq.com");
		user.setHeight(3.3f);
		
		return user;
	}
	
	@Test
	public void testRowMapper(){
		jdao.deleteAll(UserEntity.class);
		
		List<UserEntity> datalist = LangUtils.newArrayList();
		for (int i = 0; i < insertCount; i++) {
			UserEntity user = null;
			if(i>=(insertCount/2)){
				user = createUserEntity(i, "jdbc");
			}else{
				user = createUserEntity(i, "test");
			}
			datalist.add(user);
		}
		jdao.save(datalist);
		
		List<List<Object>> dblist = jdao.findList("select * from T_USER t where t.USER_NAME like :userName ", LangUtils.asMap("userName", "%test%"), List.class);
		Assert.assertNotNull(dblist);
		Assert.assertFalse(dblist.isEmpty());
		Assert.assertEquals(insertCount/2, dblist.size());
		Assert.assertEquals(ArrayList.class, dblist.get(0).getClass());
		
		List<Collection<Object>> dbcol = jdao.findList("select * from T_USER t where t.USER_NAME like :userName ", LangUtils.asMap("userName", "%test%"), Collection.class);
		Assert.assertNotNull(dbcol);
		Assert.assertFalse(dbcol.isEmpty());
		Assert.assertEquals(insertCount/2, dbcol.size());
		Assert.assertEquals(HashSet.class, dbcol.get(0).getClass());
		
		List<Object[]> dbarray = jdao.findList("select * from T_USER t where t.USER_NAME like :userName ", LangUtils.asMap("userName", "%test%"), Object[].class);
		Assert.assertNotNull(dbarray);
		Assert.assertFalse(dbarray.isEmpty());
		Assert.assertEquals(insertCount/2, dbarray.size());
		Assert.assertEquals(Object[].class, dbarray.get(0).getClass());
	}
	
}
