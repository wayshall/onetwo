package org.onetwo.cloud.feign;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import java.lang.reflect.Method;

import org.onetwo.boot.plugin.mvc.BootPluginRequestMappingCombiner.PluginContextPathChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * 如果检测到是feign client，不插入插件路径，避免污染feign client调用路径
 * @author wayshall
 * <br/>
 */
public class FeignClientPluginContextPathChecker implements PluginContextPathChecker {

	@Value("${jfish.cloud.feign.rejectPluginContextPath:true}")
	private boolean rejectPluginContextPath = true;
	
	@Override
	public boolean insertable(Method method, Class<?> handlerType, RequestMappingInfo info) {
		//如果配置了不拒绝插件路径
		if(!rejectPluginContextPath){
			return true;
		}
		EnhanceFeignClient classAnnotation = findMergedAnnotation(handlerType, EnhanceFeignClient.class);
		return classAnnotation==null;
	}
	
	

}
