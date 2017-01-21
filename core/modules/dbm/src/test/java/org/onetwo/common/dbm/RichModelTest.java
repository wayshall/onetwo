package org.onetwo.common.dbm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity.UserStatus;
import org.onetwo.common.dbm.richmodel.UserAutoidModel;
import org.onetwo.common.utils.Page;

//@Rollback(false)
public class RichModelTest extends DbmRichModelBaseTest {
	

	/*独立方法会导致UserAutoidModel在增强前加载
	 * private UserAutoidModel createUserAutoidModel(int i){
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
	}*/
	
	@Test
	public void testEnhanceMethods(){
		int i = 1;
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
		user.save();
		
		UserAutoidModel dbuser = UserAutoidModel.findById(user.getId());
		assertThat(dbuser.getUserName()).isEqualTo(user.getUserName());
		
		dbuser = UserAutoidModel.where()
									.field("id").equalTo(user.getId())
								.end()
								.toQuery()
								.one();
		assertThat(dbuser.getUserName()).isEqualTo(user.getUserName());
		
		dbuser.remove();
		List<UserAutoidModel> userlist = UserAutoidModel.findList("id", user.getId());
		assertThat(userlist).isEmpty();
		
		user.setId(null);
		user.setUserName("batch-insert-user");
		int insertCount = UserAutoidModel.batchInsert(Arrays.asList(user));
		int count = UserAutoidModel.count().intValue();
		assertThat(insertCount).isEqualTo(count);
		
		Page<UserAutoidModel> page = new Page<UserAutoidModel>();
		UserAutoidModel.findPage(page, "userName", user.getUserName());
		assertThat(page.getSize()).isEqualTo(1);
		
		UserAutoidModel.removeAll();
		
		count = UserAutoidModel.count().intValue();
		assertThat(count).isEqualTo(0);
		
	}

}
