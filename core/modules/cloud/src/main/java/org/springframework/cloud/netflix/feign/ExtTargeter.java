package org.springframework.cloud.netflix.feign;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import feign.Feign;
import feign.Target;


/**
 * 参考 HystrixFeignTargeterConfiguration 
 * 
 * @author wayshall
 * <br/>
 */
public class ExtTargeter implements Targeter, InitializingBean {

	public final static String CLASS_HYSTRIX_FEIGN = "feign.hystrix.HystrixFeign";
	
	private Targeter defaultTargeter;
	/*private ApplicationContext applicationContext;
	@Autowired
	private FeignProperties feignProperties;*/
	@Autowired(required=false)
	private TargeterEnhancer targeterEnhancer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (ClassUtils.isPresent(CLASS_HYSTRIX_FEIGN, ClassUtils.getDefaultClassLoader())) {
			defaultTargeter = new HystrixTargeter();
		} else {
			defaultTargeter = new DefaultTargeter();
		}
	}
	
	private <T> T defaultTargeter(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {
		/*String name = factory.getName();
		if (RequestUtils.isHttpPath(name)) {
			factory.setUrl(name);
			factory.setName("");
		}*/
		return defaultTargeter.target(factory, feign, context, target);
	}

	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {
		if (targeterEnhancer!=null) {
			return targeterEnhancer.enhanceTargeter(factory, ()->defaultTargeter(factory, feign, context, target));
		}
		return defaultTargeter(factory, feign, context, target);
		/*if (!feignProperties.getLocal().isEnabled()) {
			return targeterEnhancer.enhanceTargeter(applicationContext, factory, ()->{
				return defaultTargeter(factory, feign, context, target);
			});
//			return defaultTargeter(factory, feign, context, target);
		}
		
		return enhanceLocalTarget(applicationContext, factory, ()->{
			return (T)enhanceLocalTarget(applicationContext.getParent(), factory, ()->{
				return defaultTargeter(factory, feign, context, target);
			});
		});*/
		/*return getTarget(applicationContext.getParent(), factory, ()->{
			return hystrixTargeter.target(factory, feign, context, target);
		});*/
	}
	
}
