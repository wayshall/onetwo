package org.onetwo.common.spring.impl;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.spring.UserService;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.list.EasyList;
import org.onetwo.common.utils.list.L;
import org.onetwo.common.utils.list.ListFun;
import org.springframework.stereotype.Service;

import test.entity.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	private UserEntity user;
	
	@Override
	public UserEntity saveUser(UserEntity user) {
		user.setUserName("wayshall");
		user.setAge(28);
		user.setBirthDay(DateUtil.parse("1984-03-05"));
		this.user = user;
		return user;
	}
	
	@Override
	public UserEntity findUser(Long id) {
		/*if(this.user!=null && this.user.getId().equals(id))
			return this.user;*/
		UserEntity user = createUser(id);
		return user;
	}
	
	protected UserEntity createUser(Long id){
		UserEntity user = new UserEntity();
		user.setId(id);
		user.setUserName("wayshall");
		user.setAge(28);
		user.setBirthDay(DateUtil.parse("1984-03-05"));
		return user;
	}

	@Override
	public UserEntity findByName(String name) {
		UserEntity user = createUser(null);
		user.setUserName(name);
		return user;
	}

	@Override
	public Page<UserEntity> findUserByPage(Object... properties) {
		Page<UserEntity> page = new Page<UserEntity>();
		final List<UserEntity> users = new ArrayList<UserEntity>();
		L.wrapNum(1, 10).each(new ListFun<Integer>(){

			@Override
			public void exe(Integer element, int index, EasyList<Integer> easytor, Object... objects) {
				UserEntity user = new UserEntity();
				user.setUserName("wayshall");
				user.setAge(index+10);
				user.setBirthDay(DateUtil.parse("1984-03-05"));
				users.add(user);
			}
			
		});
		page.setResult(users);
		return page;
	}

	
}
