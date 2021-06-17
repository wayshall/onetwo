package org.springframework.cloud.openfeign;

import org.onetwo.cloud.feign.FeignProperties;
import org.onetwo.cloud.feign.FeignProperties.ServiceProps;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class PropsTargeterEnhancer implements TargeterEnhancer {
	@Autowired
	private FeignProperties feignProperties;

	@Override
	public <T> T enhanceTargeter(FeignTargetContext<T> ctx) {
		FeignClientFactoryBean factory = ctx.getFeignClientfactory();
		String serviceName = factory.getName();
		if (feignProperties.getServices().containsKey(serviceName)) {
			ServiceProps serviceProp = feignProperties.getServices().get(serviceName);
//			factory.setUrl(serviceProp.getUrl()); // 这里设置太迟了
			ConfigurablePropertyAccessor bw = SpringUtils.newPropertyAccessor(ctx.getHardeCodetarget(), true);
//			bw.setPropertyValue("url", serviceProp.getUrl());
		}
		return ctx.createTargeter();
	}
	
}

