package org.onetwo.project.batch.service;

import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.project.batch.entity.UserEntity;
import org.onetwo.project.batch.entity.UserVo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

@Service
public class UserProcessorService implements ItemProcessor<UserVo, UserEntity> {

	public UserEntity process(UserVo uservo) throws Exception {
		UserEntity user = new UserEntity();
		ReflectUtils.copy(uservo, user);
		return user;
	}

}
