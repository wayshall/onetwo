package org.onetwo.common.spring;

import org.onetwo.common.cache.Cacheable;
import org.onetwo.common.cache.FlushCache;
import org.onetwo.common.utils.Page;

import test.entity.UserEntity;

public interface UserService {

	@FlushCache(key="${result.id}", group="userMapForId")
	public UserEntity saveUser(UserEntity user);
	
	@Cacheable(key="${args[0]}", group="userMapForId")
	public UserEntity findUser(Long id);
	
	@Cacheable(key="${args[0]}")
	public UserEntity findByName(String name);
	
	@Cacheable(expire=3)
	public Page<UserEntity> findUserByPage(Object...properties);

}
