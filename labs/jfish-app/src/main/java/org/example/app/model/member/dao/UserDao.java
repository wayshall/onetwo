package org.example.app.model.member.dao;

import java.util.List;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.dq.annotations.Name;

public interface UserDao {

	public int save(UserEntity user);
	
	public UserEntity queryWithId(Long id);
	
	public List<UserEntity> queryListByUserNameByAge(String userName, int age);
	
	public Page<UserEntity> queryPageByUserName(Page<UserEntity> page, @Name("userName") String userName);
	
	public UserEntity queryByUserName(@Name("userName") String userName);
	
	
	public JFishQuery createUserNameQuery(@Name("userName") String userName);
	public JFishQuery deleteByUserName(@Name("userName") String userName);

}
