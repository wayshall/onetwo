package org.onetwo.dbm.core.internal;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.utils.map.CollectionMap;
import org.onetwo.dbm.annotation.DbmInterceptorFilter;
import org.onetwo.dbm.annotation.DbmInterceptorFilter.InterceptorType;
import org.onetwo.dbm.core.spi.DbmInterceptor;
import org.onetwo.dbm.core.spi.DbmInterceptorChain;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.google.common.collect.ImmutableList;

public class DbmInterceptorManager implements InitializingBean {
	
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
			InterceptorType[] types = null;
			if(filter==null){
				types = InterceptorType.values();
			}else{
				types = filter.type();
			}
			Stream.of(types).forEach(type->typeInterceptors.putElement(type, interceptor));
		}
	}
	
	public Collection<DbmInterceptor> getDbmSessionInterceptors(InterceptorType type){
		Collection<DbmInterceptor> inters = typeInterceptors.get(type);
		if(inters==null){
			inters = ImmutableList.of();
		}
		return inters;
	}

	public void setInterceptors(List<DbmInterceptor> interceptors) {
		this.interceptors = interceptors;
	}
	
	/*public Object invokeChain(InterceptorType type, Object target, Method method, Object... args){
		Collection<DbmInterceptor> inters = getDbmSessionInterceptors(type);
		DbmInterceptorChain chain = createChain(type, inters, target, method, args);
		return chain.invoke();
	}*/
	
	/*public DbmInterceptorChain createChain(InterceptorType type, Supplier<Object> actualInvoker, Object target, Method method, Object... args){
		Collection<DbmInterceptor> inters = getDbmSessionInterceptors(type);
		DbmInterceptorChain chain = createChain(type, inters, target, method, args);
		return chain;
	}
	
	public DbmInterceptorChain createChain(InterceptorType type, Supplier<Object> actualInvoker){
		Collection<DbmInterceptor> inters = getDbmSessionInterceptors(type);
		DbmInterceptorChain chain = new AbstractDbmInterceptorChain(type, inters, actualInvoker);
		return chain;
	}
	
	public DbmInterceptorChain createChain(InterceptorType type, Collection<DbmInterceptor> interceptors, Object target, Method method, Object... args){
		DbmInterceptorChain chain = new AbstractDbmInterceptorChain(type, target, method, args, interceptors);
		return chain;
	}*/

}
