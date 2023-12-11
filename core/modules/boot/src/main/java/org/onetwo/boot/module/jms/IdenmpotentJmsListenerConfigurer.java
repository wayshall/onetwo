package org.onetwo.boot.module.jms;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.onetwo.boot.module.jms.annotation.IdempotentListener;
import org.onetwo.boot.module.jms.annotation.JmsConsumer;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.SpringMergedAnnotationFinder;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

//@EnableJms
public class IdenmpotentJmsListenerConfigurer implements JmsListenerConfigurer, ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	private StringValueResolver embeddedValueResolver;
	private final AtomicInteger counter = new AtomicInteger();
	@Autowired
	private DelegateJmsMessageConsumer delegateJmsMessageConsumer;

	@Override
	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
		Map<String, ?> jmsListeners = applicationContext.getBeansWithAnnotation(JmsConsumer.class);
		jmsListeners.forEach((name, consumerBean) -> {
			findConsumerBeanMethods(consumerBean, IdempotentListener.class).forEach((listenMethod, jmsListener) -> {
//				Method invocableMethod = AopUtils.selectInvocableMethod(listenMethod, bean.getClass());
				String clientId = getEndpointId(jmsListener, listenMethod);
				SimpleJmsListenerEndpoint endPoint = new SimpleJmsListenerEndpoint();
				endPoint.setId(clientId);
				endPoint.setDestination(resolve(jmsListener.destination()));

				if (StringUtils.hasText(jmsListener.selector())) {
					endPoint.setSelector(resolve(jmsListener.selector()));
				}
				if (StringUtils.hasText(jmsListener.subscription())) {
					endPoint.setSubscription(resolve(jmsListener.subscription()));
				}
				if (StringUtils.hasText(jmsListener.concurrency())) {
					endPoint.setConcurrency(resolve(jmsListener.concurrency()));
				}

				String containerFactoryBeanName = resolve(jmsListener.containerFactory());
				JmsListenerContainerFactory<?> factory = SpringUtils.getBean(applicationContext, containerFactoryBeanName);
				
				endPoint.setMessageListener(message -> {
					JmsConsumeContext ctx = new JmsConsumeContext(clientId, message, listenMethod, consumerBean, jmsListener);
					delegateJmsMessageConsumer.processMessage(ctx);
				});
				
				registrar.registerEndpoint(endPoint, factory);
			});
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		if (applicationContext instanceof ConfigurableBeanFactory) {
			this.embeddedValueResolver = new EmbeddedValueResolver((ConfigurableBeanFactory) applicationContext);
		} else if (applicationContext.getAutowireCapableBeanFactory() instanceof ConfigurableBeanFactory) {
			this.embeddedValueResolver = new EmbeddedValueResolver((ConfigurableBeanFactory) applicationContext.getAutowireCapableBeanFactory());
		}
	}
	


	private String getEndpointId(IdempotentListener jmsListener, Method method) {
		if (StringUtils.hasText(jmsListener.id())) {
			String id = resolve(jmsListener.id());
			return (id != null ? id : "");
		}
		else {
			String id = "org.onetwo.IdempotentJmsListener-" + 
					this.counter.getAndIncrement() + ":" + 
					method.getDeclaringClass().getSimpleName() + "#" + 
					method.getName();
			return id;
		}
	}

	@Nullable
	private String resolve(String value) {
		return (this.embeddedValueResolver != null ? this.embeddedValueResolver.resolveStringValue(value) : value);
	}

	private Map<Method, IdempotentListener> findConsumerBeanMethods(Object bean, Class<? extends Annotation> consumerAnnotation){
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
		Map<Method, IdempotentListener> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
				(MethodIntrospector.MetadataLookup<IdempotentListener>) method -> {
					IdempotentListener listenerMethod = SpringMergedAnnotationFinder.INSTANCE.getAnnotation(method, IdempotentListener.class);
					return listenerMethod;
				});
		return annotatedMethods;
	}
	
//	static List<Method> findConsumersMethods(Class<?> targetClass, Class<? extends Annotation> consumerAnnotation){
//		List<Method> consumerMethods = Lists.newArrayList();
//		ReflectionUtils.doWithMethods(targetClass, m->consumerMethods.add(m), m->{
//			Annotation anno = SpringMergedAnnotationFinder.INSTANCE.getAnnotation(m, consumerAnnotation);
//			if(SpringMergedAnnotationFinder.INSTANCE.getAnnotation(m, consumerAnnotation)!=null){
//				return true;
//			}
//			return false;
//		});
//		return consumerMethods;
//	}

}
