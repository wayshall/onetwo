package org.springframework.cloud.openfeign;

import feign.Feign;
import feign.Feign.Builder;
import feign.Target;
import feign.Target.HardCodedTarget;
import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class FeignTargetContext<T> {
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
