package org.onetwo.cloud.bugfix;

import org.onetwo.boot.core.web.mvc.ExtRequestMappingHandlerMapping;
import org.onetwo.cloud.util.BootCloudUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class FixFeignClientsHandlerMapping extends ExtRequestMappingHandlerMapping {

	@Override
	protected boolean isHandler(Class<?> beanType) {
//		if (beanType.getName().contains("CaptchaController")) {
//			System.out.println("test");
//		}
		//避免springmvc 把抽象出来的带有RequestMapping注解的client接口扫描到controller注册
		boolean isFeignClient = AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
		if(BootCloudUtils.isNetflixFeignClientPresent() && isFeignClient){
			if(log.isInfoEnabled()){
				log.info("ignore FeignClient: {}", beanType);
			}
			return false;
		}
		return super.isHandler(beanType);
	}
}
