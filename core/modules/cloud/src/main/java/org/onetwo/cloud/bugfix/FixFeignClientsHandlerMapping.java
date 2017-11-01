package org.onetwo.cloud.bugfix;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.core.web.mvc.ExtRequestMappingHandlerMapping;
import org.onetwo.cloud.util.BootCloudUtils;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class FixFeignClientsHandlerMapping extends ExtRequestMappingHandlerMapping {

	@Override
	protected boolean isHandler(Class<?> beanType) {
		//避免springmvc 把client扫描到controller注册
		if(BootCloudUtils.isNetflixFeignClientPresent() && AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class)){
			if(log.isInfoEnabled()){
				log.info("ignore FeignClient: {}", beanType);
			}
			return false;
		}
		return super.isHandler(beanType);
	}
}
