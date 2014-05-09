package org.onetwo.plugins.security.common;

import java.util.concurrent.TimeUnit;

import org.onetwo.common.utils.SessionStorer;
import org.onetwo.common.utils.UserDetail;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class MemorySessionStorer implements SessionStorer {
//	private Map<String, UserDetail> users = LangUtils.newHashMap();
	private Cache<String, UserDetail> users = CacheBuilder.newBuilder()
														.expireAfterAccess(60, TimeUnit.MINUTES)
														.build();

	public void addUser(String token, UserDetail userDetail){
		users.put(token, userDetail);
	}

	@Override
	public UserDetail getUser(String token) {
		return users.getIfPresent(token);
	}

	@Override
	public UserDetail removeUser(String token) {
		UserDetail user = getUser(token);
		users.invalidate(token);
		return user;
	}
	
}
