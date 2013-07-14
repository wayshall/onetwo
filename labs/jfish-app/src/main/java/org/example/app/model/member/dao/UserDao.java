package org.example.app.model.member.dao;

import java.util.List;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.dq.annotations.Name;

public interface UserDao {

	/***
	 * 通过对象的属性
	 * @param user
	 * @return
	 */
	public int save(UserEntity user);
	
	/****
	 * 通过命名索引，比如这里是 id=:0
	 * @param id
	 * @return
	 */
	public UserEntity queryWithId(Long id);
	
	/****
	 * 通过by分割参数名称
	 * @param userName
	 * @param age
	 * @return
	 */
	public List<UserEntity> queryListByUserNameByAge(String userName, int age);
	
	/****
	 * 通过注解定义参数名
	 * @param page
	 * @param userName
	 * @return
	 */
	public Page<UserEntity> queryPageByUserName(Page<UserEntity> page, @Name("userName") String userName);
	
	public UserEntity queryByUserName(@Name("userName") String userName);
	
	/*****
	 * create, delete 等前缀会执行executeUpdate方法
	 * @param userName
	 * @return
	 */
	public JFishQuery createUserNameQuery(@Name("userName") String userName);
	public JFishQuery deleteByUserName(@Name("userName") String userName);

}
