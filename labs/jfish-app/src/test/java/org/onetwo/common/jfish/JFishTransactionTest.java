package org.onetwo.common.jfish;

import javax.annotation.Resource;

import org.example.app.model.member.entity.UserEntity;
import org.example.app.model.member.service.impl.TuserServiceImpl;
import org.junit.Test;
import org.onetwo.common.fish.spring.JFishDaoImpl;
import org.onetwo.common.utils.DateUtil;

public class JFishTransactionTest extends JFishBaseJUnitTest {

	@Resource
	private JFishDaoImpl jdao;

	@Resource
	private TuserServiceImpl tuserServiceImpl;
	
	protected UserEntity createUserEntity(int i, String userName){
		UserEntity user = new UserEntity();
		user.setUserName(userName+i);
		user.setBirthDay(DateUtil.now());
		user.setEmail(i+"username@qq.com");
		user.setHeight(3.3f);
		
		return user;
	}
	
	@Test
	public void testDirty(){
		UserEntity user = new UserEntity();
		user.setId(480L);
		user.setUserName("way33");
		user.setBirthDay(DateUtil.now());
		user.setEmail("username33@qq.com");
		user.setPassword("123456");
		user.setHeight(3.3f);
		user.setAge(28);
		user.setReadOnlyField("testValue");
		tuserServiceImpl.save(user);
		System.out.println("id:" + user.getId());
	}
}
