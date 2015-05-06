package org.onetwo.test.jorm.model.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.test.jorm.AppBaseTest;
import org.onetwo.test.jorm.model.entity.UserAutoidEntity;

public class UserAutoidServiceTest extends AppBaseTest {
	
	@Resource
	private UserAutoidServiceImpl userAutoidServiceImpl;
	
	@Test
	public void testCrud(){
		this.userAutoidServiceImpl.deleteAll();
		
		String userNamePrefix = "userName";
		Date now = new Date();
		System.out.println("now: " + NiceDate.New(now).formatDateTimeMillis());
		
		//精确到秒，否则会有误差，比如2015-05-06 13:49:09.783存储到mysql后会变成2015-05-06 13:49:10，mysql的datetime只能精确到秒
		NiceDate niceNowSeconde = NiceDate.New(now).thisSec();
		System.out.println("niceNowSeconde: " + niceNowSeconde.formatDateTimeMillis());
		int count = 10;
		int insertCount = this.userAutoidServiceImpl.saveList(userNamePrefix, niceNowSeconde.getTime(), count);
		Assert.assertEquals(10, insertCount);
		
		List<UserAutoidEntity> userlist = this.userAutoidServiceImpl.findUserAutoIdEntity(userNamePrefix, niceNowSeconde.getTime());
		Assert.assertEquals(count, userlist.size());
		
		final String newUserNamePrefix = "update-user-name";
		userlist.stream().forEach(e->( e.setUserName(newUserNamePrefix)));
		count = this.userAutoidServiceImpl.update(userlist);
		Assert.assertEquals(count, userlist.size());
		
		userlist = this.userAutoidServiceImpl.findUserAutoIdEntity(newUserNamePrefix, niceNowSeconde.getTime());
		Assert.assertEquals(count, userlist.size());
		
		count = this.userAutoidServiceImpl.delete(userlist);
		Assert.assertEquals(count, userlist.size());
	}

}
