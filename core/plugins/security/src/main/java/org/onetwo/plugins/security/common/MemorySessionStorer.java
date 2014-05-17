package org.onetwo.plugins.security.common;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.SessionStorer;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.plugins.security.server.SsoServerConfig;
import org.slf4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class MemorySessionStorer implements SessionStorer {
	
	private static final Logger logger = MyLoggerFactory.getLogger(MemorySessionStorer.class);
	
//	private Map<String, UserDetail> users = LangUtils.newHashMap();
	private Cache<String, UserDetail> users;

	@Resource
	private SsoServerConfig ssoServerConfig;
	
	public MemorySessionStorer(){
	}
	
	@PostConstruct
	public void init(){
		long timeout = ssoServerConfig.getTimeout();
		users = CacheBuilder.newBuilder()
		.expireAfterAccess(timeout, TimeUnit.MINUTES)
//		.expireAfterWrite(timeout, TimeUnit.SECONDS)
		//TODO don't work
		.removalListener(new RemovalListener<String, UserDetail>(){

			@Override
			public void onRemoval(RemovalNotification<String, UserDetail> notification) {
				logger.info("====>>user[{}] has remove..", notification.getValue().getUserId());
			}
			
		})
		.build();
	}
	
	public void addUser(String token, UserDetail userDetail){
		Assert.notNull(token);
		users.put(token, userDetail);
	}

	@Override
	public UserDetail getUser(String token) {
		return token==null?null:users.getIfPresent(token);
	}

	@Override
	public UserDetail removeUser(String token) {
		if(token==null)
			return null;
		UserDetail user = getUser(token);
		if(user!=null)
			users.invalidate(token);
		return user;
	}
	
}
