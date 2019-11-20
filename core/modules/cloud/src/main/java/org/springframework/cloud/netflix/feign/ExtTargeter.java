package org.springframework.cloud.netflix.feign;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import feign.Feign;
import feign.Feign.Builder;
import feign.Target;
import feign.Target.HardCodedTarget;
import lombok.Data;


/**
 * 参考 HystrixFeignTargeterConfiguration 
 * 
 * @author wayshall
 * <br/>
 */
public class ExtTargeter implements Targeter, InitializingBean {

	public final static String CLASS_HYSTRIX_FEIGN = "feign.hystrix.HystrixFeign";
	
	private Targeter cloudTargeter;
	/*private ApplicationContext applicationContext;
	@Autowired
	private FeignProperties feignProperties;*/
	@Autowired(required=false)
	private TargeterEnhancer targeterEnhancer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (ClassUtils.isPresent(CLASS_HYSTRIX_FEIGN, ClassUtils.getDefaultClassLoader())) {
			cloudTargeter = new HystrixTargeter();
		} else {
			cloudTargeter = new DefaultTargeter();
		}
	}
	
	/*private <T> T defaultTargeter(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {
		return cloudTargeter.target(factory, feign, context, target);
	}*/

	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {
		FeignTargetContext<T> ctx = new FeignTargetContext<>(factory, feign, context, target, cloudTargeter);
		if (targeterEnhancer!=null) {
			return targeterEnhancer.enhanceTargeter(ctx);
		}
		return ctx.createTargeter();
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
	
	@Data
	public static class FeignTargetContext<T> {
		private FeignClientFactoryBean feignClientfactory;
		private Feign.Builder feign;
		private FeignContext context;
		private Target.HardCodedTarget<T> hardeCodetarget;
		private Targeter cloudTargeter;
		
		public FeignTargetContext(FeignClientFactoryBean feignClientfactory, Builder feign, FeignContext context,
				HardCodedTarget<T> hardeCodetarget, Targeter cloudTargeter) {
			super();
			this.feignClientfactory = feignClientfactory;
			this.feign = feign;
			this.context = context;
			this.hardeCodetarget = hardeCodetarget;
			this.cloudTargeter = cloudTargeter;
		}
		
		public T createTargeter() {
			/*String name = factory.getName();
			if (RequestUtils.isHttpPath(name)) {
				factory.setUrl(name);
				factory.setName("");
			}*/
			return cloudTargeter.target(feignClientfactory, feign, context, hardeCodetarget);
		}
	}
	
}
