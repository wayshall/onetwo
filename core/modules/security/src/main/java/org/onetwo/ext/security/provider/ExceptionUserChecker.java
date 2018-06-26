package org.onetwo.ext.security.provider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangOps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**异常用户检测，如果用户在某一个时段内连续登录失败超过maxLoginTimes次，则拒绝再尝试。
 * @author wayshall
 * <br/>
 */
@Slf4j
public class ExceptionUserChecker implements InitializingBean, AuthenticationProvider {
	
	private Cache<String, AtomicInteger> exceptionUsers;
	/***
	 * 默认一天
	 */
	@Setter
	private String duration = "1d";
	/***
	 * 默认最大5次
	 */
	@Setter
	private int maxLoginTimes = 5;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(duration, "duration must have length; it must not be null or empty");
		Pair<Integer, TimeUnit> durationPair = LangOps.parseTimeUnit(duration);
		exceptionUsers = CacheBuilder.newBuilder()
									  .maximumSize(100)
//									  .expireAfterAccess(durationPair.getKey(), durationPair.getValue())
									  .expireAfterWrite(durationPair.getKey(), durationPair.getValue())
									  .build();
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		checkUser(userName);
		return null;
	}
	
	
	public void checkUser(String userName){
		AtomicInteger errorTimes = getExceptionTimesByUser(userName);
		int times = errorTimes.get();
		if(times>=maxLoginTimes){
			throw new LockedException("登录错误超过"+maxLoginTimes+"次，请稍后尝试！");
		}
	}
	
	@EventListener
	public void onBadCredentials(AuthenticationFailureBadCredentialsEvent event){
		String userName = event.getAuthentication().getName();
		AtomicInteger errorTimes = getExceptionTimesByUser(userName);
		int times = errorTimes.incrementAndGet();
		if(log.isWarnEnabled()){
			log.warn("The user[{}] has logged in {} times failed", userName, times);
		}
	}
	
	public AtomicInteger getExceptionTimesByUser(String userName){
		try {
			return exceptionUsers.get(userName, ()->new AtomicInteger(0));
		} catch (ExecutionException e) {
			throw new BaseException("getExceptionTimesByUser error: " + e.getMessage(), e);
		}
	}


	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
