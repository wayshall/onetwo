package org.springframework.cloud.netflix.feign;

import org.springframework.util.Assert;

import feign.Feign;
import feign.Target;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;


/**
 * 复制自HystrixTargeter
 * 去掉了返回类型限制
 * @author wayshall
 * <br/>
 */
public class CopyOfEnhanceHystrixTargeter implements Targeter {
	
	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,
						Target.HardCodedTarget<T> target) {
		if (!(feign instanceof feign.hystrix.HystrixFeign.Builder)) {
			return feign.target(target);
		}
		feign.hystrix.HystrixFeign.Builder builder = (feign.hystrix.HystrixFeign.Builder) feign;
		Class<?> fallback = factory.getFallback();
		if (fallback != void.class) {
			return targetWithFallback(factory.getName(), context, target, builder, fallback);
		}
		Class<?> fallbackFactory = factory.getFallbackFactory();
		if (fallbackFactory != void.class) {
			return targetWithFallbackFactory(factory.getName(), context, target, builder, fallbackFactory);
		}

		return feign.target(target);
	}

	private <T> T targetWithFallbackFactory(String feignClientName, FeignContext context,
											Target.HardCodedTarget<T> target,
											HystrixFeign.Builder builder,
											Class<?> fallbackFactoryClass) {
		FallbackFactory<? extends T> fallbackFactory = (FallbackFactory<? extends T>)
			getFromContext("fallbackFactory", feignClientName, context, fallbackFactoryClass, FallbackFactory.class);
		/* We take a sample fallback from the fallback factory to check if it returns a fallback
		that is compatible with the annotated feign interface. */
		Object exampleFallback = fallbackFactory.create(new RuntimeException());
		Assert.notNull(exampleFallback,
			String.format(
			"Incompatible fallbackFactory instance for feign client %s. Factory may not produce null!",
				feignClientName));
		/*if (!target.type().isAssignableFrom(exampleFallback.getClass())) {
			throw new IllegalStateException(
				String.format(
					"Incompatible fallbackFactory instance for feign client %s. Factory produces instances of '%s', but should produce instances of '%s'",
					feignClientName, exampleFallback.getClass(), target.type()));
		}*/
		return builder.target(target, fallbackFactory);
	}


	private <T> T targetWithFallback(String feignClientName, FeignContext context,
									 Target.HardCodedTarget<T> target,
									 HystrixFeign.Builder builder, Class<?> fallback) {
		T fallbackInstance = getFromContext("fallback", feignClientName, context, fallback, target.type());
		return builder.target(target, fallbackInstance);
	}

	private <T> T getFromContext(String fallbackMechanism, String feignClientName, FeignContext context,
								 Class<?> beanType, Class<T> targetType) {
		Object fallbackInstance = context.getInstance(feignClientName, beanType);
		if (fallbackInstance == null) {
			throw new IllegalStateException(String.format(
				"No " + fallbackMechanism + " instance of type %s found for feign client %s",
				beanType, feignClientName));
		}

		if (!targetType.isAssignableFrom(beanType)) {
			throw new IllegalStateException(
					String.format(
						"Incompatible " + fallbackMechanism + " instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
						beanType, targetType, feignClientName));
		}
		return (T) fallbackInstance;
	}	

}
