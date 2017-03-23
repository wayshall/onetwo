package org.onetwo.dbm.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.utils.map.CollectionMap;
import org.onetwo.dbm.interceptor.annotation.DbmInterceptorFilter;
import org.onetwo.dbm.interceptor.annotation.DbmInterceptorFilter.InterceptorType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.google.common.collect.ImmutableList;

public class DbmInterceptorManager implements InitializingBean {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private List<DbmInterceptor> interceptors;
	private CollectionMap<InterceptorType, DbmInterceptor> typeInterceptors = CollectionMap.newListMap();

	@Override
	public void afterPropertiesSet() throws Exception {
//		this.dbmInterceptors = SpringUtils.getBeans(applicationContext, DbmInterceptor.class);
		if(this.interceptors==null){
			this.interceptors = Collections.emptyList();
			return ;
		}
		
		AnnotationAwareOrderComparator.sort(interceptors);
		
		for(DbmInterceptor interceptor : this.interceptors){
			DbmInterceptorFilter filter = AnnotationUtils.findAnnotationWithSupers(interceptor.getClass(), DbmInterceptorFilter.class);
			if(filter==null){
				Stream.of(InterceptorType.values()).forEach(type->typeInterceptors.putElement(type, interceptor));
			}else{
				typeInterceptors.putElement(filter.type(), interceptor);
			}
		}
	}
	
	public Collection<DbmInterceptor> getDbmSessionInterceptors(InterceptorType type){
		Collection<DbmInterceptor> inters = typeInterceptors.get(type);
		if(inters==null){
			inters = ImmutableList.of();
		}
		return inters;
	}
	
	public Object invokeChain(InterceptorType type, Object target, Method method, Object... args){
		Collection<DbmInterceptor> inters = getDbmSessionInterceptors(type);
		DbmInterceptorChain chain = createChain(inters, target, method, args);
		return chain.invoke();
	}
	
	public DbmInterceptorChain createChain(InterceptorType type, Object target, Method method, Object... args){
		Collection<DbmInterceptor> inters = getDbmSessionInterceptors(type);
		DbmInterceptorChain chain = createChain(inters, target, method, args);
		return chain;
	}
	
	public DbmInterceptorChain createChain(InterceptorType type, Supplier<Object> actualInvoker){
		Collection<DbmInterceptor> inters = getDbmSessionInterceptors(type);
		DbmInterceptorChain chain = new DefaultDbmInterceptorChain(inters, actualInvoker);
		return chain;
	}
	public DbmInterceptorChain createChain(Collection<DbmInterceptor> interceptors, Object target, Method method, Object... args){
		DbmInterceptorChain chain = new DefaultDbmInterceptorChain(target, method, args, interceptors);
		return chain;
	}

}
