package org.onetwo.project.batch.service;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.project.batch.entity.UserEntity;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

@Service
public class UserWriterService implements ItemWriter<UserEntity> {
	
	@Resource
	private BaseEntityManager baseEntityManager;

	public void write(List<? extends UserEntity> users) throws Exception {
		for(UserEntity user : users){
			baseEntityManager.save(user);
		}
		try {
			baseEntityManager.flush();
		} finally {
			baseEntityManager.clear();
		}
	}
	
	

}
