package org.onetwo.common.ds;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.ds.SwitcherInfo.Type;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class SwitcherProxyImpl implements InitializingBean, SwitcherProxy {
	
	@Resource
	private ContextHolder contextHolder;
	private Cache<Object, SwitcherInfo> switcherCaches = CacheBuilder.newBuilder().build();
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(contextHolder, "contextHolder can not be null.");
	}
	
	@Override
	public void processSwitchInfo(JoinPoint pjp){
		final MethodSignature ms = (MethodSignature) pjp.getSignature();
		SwitcherInfo info = null;
		
		info = contextHolder.getContextAttribute(SwitcherInfo.CURRENT_SWITCHER_INFO);
		if(info!=null)
			return ;
		
		try {
			info = this.switcherCaches.get(ms.getMethod(), new Callable<SwitcherInfo>() {

				@Override
				public SwitcherInfo call() throws Exception {
					SwitcherInfo switcherInfo = null;
					Switcher dsw = AnnotationUtils.findAnnotationWithStopClass(ms.getDeclaringType(), ms.getMethod(), Switcher.class, Object.class);
					
					if(dsw==null){
						Transactional tnl = AnnotationUtils.findAnnotationWithStopClass(ms.getDeclaringType(), ms.getMethod(), Transactional.class, Object.class);
						if(tnl!=null){
							switcherInfo = new SwitcherInfo(tnl.value(), Type.TransactionManager);
						}else{
							switcherInfo = SwitcherInfo.DEFAULT_INFO;
						}
					}else{
						switcherInfo = new SwitcherInfo(dsw.value());
					}
					return switcherInfo;
				}
				
			});
		} catch (ExecutionException e) {
			throw new BaseException("get switcher error: " + ms.getMethod());
		}
		contextHolder.setContextAttribute(SwitcherInfo.CURRENT_SWITCHER_INFO, info);
	}
	
	protected void clearSwitchInfo(){
		contextHolder.setContextAttribute(SwitcherInfo.CURRENT_SWITCHER_INFO, null);
	}

}
