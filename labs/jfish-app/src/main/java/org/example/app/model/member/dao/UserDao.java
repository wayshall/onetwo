package org.example.app.model.member.dao;

import java.util.List;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.dq.annotations.QueryInterface;

@QueryInterface
public interface UserDao {

	/*@DynamicQuery
	public UserEntity findById(Long userId);
	@DynamicQuery
	public UserEntity findByIdAndUserName(Long userId, String userName);*/
	
	public UserEntity queryById(Long userId);
	public List<UserEntity> queryListByUserName(String userName);
	public Page<UserEntity> queryPageByUserName(String userName);
	public DataQuery queryByUserName(String userName);

}
