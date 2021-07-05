package org.onetwo.cloud.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 主要修复一些特殊问题
 * 
 * @author wayshall
 * <br/>
 */
@Configuration
//@AutoConfigureBefore(FeignAutoConfiguration.classS)
public class CornerFeignConfiguration {
	
	public static final String KEY_REJECT_PLUGIN_CONTEXT_PATH = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".cloud.feign.rejectPluginContextPath";

	@Bean
	public FeignClientPluginContextPathChecker feignClientPluginContextPathChecker(){
		return new FeignClientPluginContextPathChecker();
	}
	
	/*@Bean
	public PluginContextPathMetaAppender pluginContextPathMetaAppender() {
		return new PluginContextPathMetaAppender();
	}
	

	@Bean
	@ConditionalOnProperty(name=KEY_REJECT_PLUGIN_CONTEXT_PATH, matchIfMissing=false)
	public RibbonLoadBalancerContext ribbonLoadBalancerContext(ILoadBalancer loadBalancer, IClientConfig config, RetryHandler retryHandler) {
		return new RibbonLoadBalancerContext(loadBalancer, config, retryHandler);
	}*/
}
