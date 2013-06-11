package org.example.app.model.member.dao;

import java.util.List;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.dq.annotations.Name;

public interface UserDao {

	abstract public JFishQuery save(UserEntity user);
	
	abstract public UserEntity queryById(Long id);
	abstract public List<UserEntity> queryListByUserName(@Name("userName") String userName);
	abstract public Page<UserEntity> queryPageByUserName(Page<UserEntity> page, @Name("userName") String userName);
	abstract public UserEntity queryByUserName(@Name("userName") String userName);
	abstract public JFishQuery createUserNameQuery(@Name("userName") String userName);
	abstract public JFishQuery deleteByUserName(@Name("userName") String userName);

}
