package org.onetwo.common.dbm;

import java.util.Date;

import org.junit.Test;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity.UserStatus;
import org.onetwo.common.dbm.richmodel.UserAutoidModel;

public class RichModelTest extends DbmRichModelBaseTest {
	

	private UserAutoidModel createUserAutoidModel(int i){
		String userNamePrefix = "RichModelTest";;
		UserAutoidModel user = new UserAutoidModel();
		user.setUserName(userNamePrefix+"-insert-"+i);
		user.setPassword("password-insert-"+i);
		user.setGender(i%2);
		user.setNickName("nickName-insert-"+i);
		user.setEmail("test@qq.com");
		user.setMobile("137"+i);
		user.setBirthday(new Date());
		user.setStatus(UserStatus.NORMAL);
		return user;
	}
	
	@Test
	public void testSave(){
		UserAutoidModel user = createUserAutoidModel(1);
		user.save();
	}

}
