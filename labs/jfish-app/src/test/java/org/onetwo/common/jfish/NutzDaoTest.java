package org.onetwo.common.jfish;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.example.app.model.member.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.nutz.NutzBaseDao;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.onetwo.common.utils.DateUtil;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ActiveProfiles({"nutz", "test"})
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback=false)
public class NutzDaoTest extends SpringTxJUnitTestCase {

	@Resource
	private DataSource dataSource;
	private NutzBaseDao nutzDao;

	private int insertCount = 10000;
	
	@Before
	public void setup(){
		nutzDao = new NutzBaseDao(dataSource);
	}
	@Test
	public void testSave(){
		UtilTimerStack.setActive(true);
		String name = "NutzDaoTest";
		UtilTimerStack.push(name);
		for(int i=0; i<insertCount; i++){
			UserEntity user = new UserEntity();
			user.setUserName("NutzDaoTest");
			user.setBirthDay(DateUtil.now());
			user.setEmail("username@qq.com");
			user.setHeight(3.3f);

			nutzDao.insert(user);
			
		}
		UtilTimerStack.pop(name);
	}
}
