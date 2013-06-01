package org.example.app.model.member.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.example.app.model.member.entity.UserEntity;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.cache.CacheKeys;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*****
 * 假如不要dao，service可以直接继承JFishCrudServiceImpl 以获得更多方便查询的接口
 * 
 * @author wayshall
 * 
 */
@Service
public class TestCacheServiceImpl {

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	protected JFishEntityManager jem;

	@Transactional(readOnly = true)
	@Cacheable(value=CacheKeys.DYNAMIC_DATA_METHOD_CACHE, key="'findPageByName'+#keyword")
	public Page<UserEntity> findPageByName(Page<UserEntity> page, String keyword) {
		List<UserEntity> users = createUserList(100);
		page.setResult(users);
		page.setTotalCount(users.size());
		return page;
	}

	@Transactional(readOnly = true)
	@Cacheable(value=CacheKeys.DYNAMIC_DATA_METHOD_CACHE, key="'findListByName'+#keyword")
	public List<UserEntity> findListByName(String keyword) {
		List<UserEntity> users = createUserList(100);
		return users;
	}

	private List<UserEntity> createUserList(int count) {
		List<UserEntity> list = LangUtils.newArrayList(count);
		UserEntity user = null;
		for (int i = 0; i < count; i++) {
			user = new UserEntity();
			user.setUserName("way"+i);
			user.setBirthDay(new Date());
			user.setEmail("way"+i+"@qq.com");
			user.setHeight(163.0f);
			user.setAge(28);
			user.setId(Long.valueOf(i));
			
			list.add(user);
		}
		logger.info("createUserList count : " + count);
		
		return list;
	}

}
