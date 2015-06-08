package appweb.admin.model.service.impl;

import java.util.List;

import org.onetwo.common.utils.Langs;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.Page;
import org.springframework.stereotype.Service;

import appweb.rpc.service.JsonRpcServiceTest;
import appweb.rpc.vo.UserVo;

@Service
public class JsonRpcServiceTestImpl implements JsonRpcServiceTest {
	
	@Override
	public String say(String something){
		return "helllo "+ something;
	}

	@Override
	public Long sum(int num1, int num2) {
		return (long)num1+num2;
	}

	@Override
	public List<UserVo> findUsersByUserNameLike(String userName, int limited) {
		return Langs.generateList(limited, i->{
			UserVo user = new UserVo();
			user.setId(i);
			user.setAge(30+i);
			user.setUserName(userName+i);
			user.setBirthday(NiceDate.New().nextDay(i).getTime());
			return user;
		});
	}

	@Override
	public Page<UserVo> findUsersPageByUserNameLike(String userName, int limited) {
		List<UserVo> userList = findUsersByUserNameLike(userName, limited);
		Page<UserVo> page = new Page<UserVo>();
		page.setResult(userList);
		page.setTotalCount(1000);
		return page;
	}

}
