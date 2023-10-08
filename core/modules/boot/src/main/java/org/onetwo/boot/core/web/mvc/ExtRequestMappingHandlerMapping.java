package org.onetwo.boot.core.web.mvc;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.onetwo.common.utils.CUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


/**********
 * 
 * @author wayshall
 *
 */
public class ExtRequestMappingHandlerMapping extends RequestMappingHandlerMapping implements InitializingBean {

	@Autowired(required=false)
	private List<RequestMappingCombiner> combiners;

	@Override
	public void afterPropertiesSet() {
		List<RequestMappingCombiner> combiners = this.combiners;
		if(combiners==null){
			combiners = Lists.newArrayList();
		}
		if(!combiners.isEmpty()){
//			combiners.sort(OrderComparator.INSTANCE);
			AnnotationAwareOrderComparator.sort(combiners);
		}
		this.combiners = ImmutableList.copyOf(combiners);
		super.afterPropertiesSet();
	}
	
	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		for(RequestMappingCombiner cb : this.combiners){
			info = cb.combine(method, handlerType, info);
		}
		return info;
	}
	
	@Override
	protected void detectMappedInterceptors(List<HandlerInterceptor> mappedInterceptors) {
		super.detectMappedInterceptors(mappedInterceptors);
		CUtils.stripNull(mappedInterceptors);
		Collections.sort(mappedInterceptors, new Comparator<HandlerInterceptor>() {

			@Override
			public int compare(HandlerInterceptor o1, HandlerInterceptor o2) {
				return OrderComparator.INSTANCE.compare(o1, o2);
			}
			
		});
	}
	
	public static interface RequestMappingCombiner {
		RequestMappingInfo combine(Method method, Class<?> handlerType, RequestMappingInfo info);
		
		static RequestMappingInfo createRequestMappingInfo(String path, Method method, Class<?> handlerType, RequestMappingInfo info) {
//			BuilderConfiguration config = new BuilderConfiguration();
//			config.setPathMatcher(new AntPathMatcher());
//			config.setPatternParser(PathPatternParser.defaultInstance);
//			return RequestMappingInfo.paths(path)
////									.options(config)
//									.build()
//									.mutate()
//									.build();
			return info.mutate().paths(path).build();
//			return new RequestMappingInfo(
//					new PatternsRequestCondition(path),
//					info.getMethodsCondition(),
//					info.getParamsCondition(),
//					info.getHeadersCondition(),
//					info.getConsumesCondition(), 
//					info.getProducesCondition(),
//					info.getCustomCondition());
		}
	}

}
