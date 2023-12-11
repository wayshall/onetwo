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
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.MethodJmsListenerEndpoint;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

//@EnableJms
public class IdenmpotentJmsListenerConfigurer implements JmsListenerConfigurer, ApplicationContextAware, BeanFactoryAware {
	
	private ApplicationContext applicationContext;
	private BeanFactory beanFactory;
	private MessageHandlerMethodFactory messageHandlerMethodFactory;

	private StringValueResolver embeddedValueResolver;
	private final AtomicInteger counter = new AtomicInteger();
	@Autowired
	private DelegateJmsMessageConsumer delegateJmsMessageConsumer;
//	private JmsListenerEndpointRegistrar registrar;

	@Override
	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
//		this.registrar = registrar;
//		registrar.setBeanFactory(beanFactory);
		this.messageHandlerMethodFactory = createDefaultJmsHandlerMethodFactory();
		
		Map<String, ?> jmsListeners = applicationContext.getBeansWithAnnotation(JmsConsumer.class);
		jmsListeners.forEach((name, consumerBean) -> {
			// MessageListenerAdapter
			Map<Method, IdempotentListener> listenerMethods = findConsumerBeanMethods(consumerBean, IdempotentListener.class);
//			if (listenerMethods.isEmpty()) {
//				MessageListenerAdapter adapter = new MessageListenerAdapter(jmsListeners);
//			}
			
			listenerMethods.forEach((listenMethod, jmsListener) -> {
//				Method invocableMethod = AopUtils.selectInvocableMethod(listenMethod, bean.getClass());
//				String clientId = getEndpointId(jmsListener, listenMethod);
				JmsListenerEndpoint endpoint = createJmsListenerEndpoint(consumerBean, listenMethod, jmsListener);

				JmsListenerContainerFactory<?> factory = null;
				String containerFactoryBeanName = resolve(jmsListener.containerFactory());
				if (StringUtils.hasText(containerFactoryBeanName)) {
					Assert.state(this.beanFactory != null, "BeanFactory must be set to obtain container factory by bean name");
					try {
						factory = this.beanFactory.getBean(containerFactoryBeanName, JmsListenerContainerFactory.class);
					}
					catch (NoSuchBeanDefinitionException ex) {
						throw new BeanInitializationException("Could not register JMS listener endpoint on [" +
								listenMethod + "], no " + JmsListenerContainerFactory.class.getSimpleName() +
								" with id '" + containerFactoryBeanName + "' was found in the application context", ex);
					}
				}
				registrar.registerEndpoint(endpoint, factory);
			});
		});
	}
	
	/****
	 * @see org.springframework.jms.annotation.JmsListenerAnnotationBeanPostProcessor#processJmsListener
	 * @param bean
	 * @param listenMethod
	 * @param jmsListener
	 * @return
	 */
	protected JmsListenerEndpoint createJmsListenerEndpoint(Object bean, Method listenMethod, IdempotentListener jmsListener) {
		Method invocableMethod = AopUtils.selectInvocableMethod(listenMethod, bean.getClass());
		MethodJmsListenerEndpoint endpoint = new MethodJmsListenerEndpoint();
		
		endpoint.setBean(bean);
		endpoint.setMethod(invocableMethod);
		endpoint.setMostSpecificMethod(listenMethod);
		endpoint.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory);
		endpoint.setEmbeddedValueResolver(this.embeddedValueResolver);
		endpoint.setBeanFactory(this.beanFactory);
		endpoint.setId(getEndpointId(jmsListener, listenMethod));
		endpoint.setDestination(resolve(jmsListener.destination()));
		if (StringUtils.hasText(jmsListener.selector())) {
			endpoint.setSelector(resolve(jmsListener.selector()));
		}
		if (StringUtils.hasText(jmsListener.subscription())) {
			endpoint.setSubscription(resolve(jmsListener.subscription()));
		}
		if (StringUtils.hasText(jmsListener.concurrency())) {
			endpoint.setConcurrency(resolve(jmsListener.concurrency()));
		}

		return endpoint;
	}
	
//	protected JmsListenerEndpoint createJmsListenerEndpoint_bak(Object consumerBean, Method listenMethod, IdempotentListener jmsListener) {
//		String clientId = getEndpointId(jmsListener, listenMethod);
//		SimpleJmsListenerEndpoint endPoint = new SimpleJmsListenerEndpoint();
//		endPoint.setId(clientId);
//		endPoint.setDestination(resolve(jmsListener.destination()));
//
//		if (StringUtils.hasText(jmsListener.selector())) {
//			endPoint.setSelector(resolve(jmsListener.selector()));
//		}
//		if (StringUtils.hasText(jmsListener.subscription())) {
//			endPoint.setSubscription(resolve(jmsListener.subscription()));
//		}
//		if (StringUtils.hasText(jmsListener.concurrency())) {
//			endPoint.setConcurrency(resolve(jmsListener.concurrency()));
//		}
//
////		String containerFactoryBeanName = resolve(jmsListener.containerFactory());
////		JmsListenerContainerFactory<?> factory = SpringUtils.getBean(applicationContext, containerFactoryBeanName);
//		
//		endPoint.setMessageListener(message -> {
//			JmsConsumeContext ctx = new JmsConsumeContext(clientId, message, listenMethod, consumerBean, jmsListener);
//			delegateJmsMessageConsumer.processMessage(ctx);
//		});
//		
//		return endPoint;
//	}

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

	private MessageHandlerMethodFactory createDefaultJmsHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory defaultFactory = new DefaultMessageHandlerMethodFactory();
		if (beanFactory != null) {
			defaultFactory.setBeanFactory(beanFactory);
		}
		defaultFactory.afterPropertiesSet();
		return defaultFactory;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		if (beanFactory instanceof ConfigurableBeanFactory) {
			this.embeddedValueResolver = new EmbeddedValueResolver((ConfigurableBeanFactory) beanFactory);
		}
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
