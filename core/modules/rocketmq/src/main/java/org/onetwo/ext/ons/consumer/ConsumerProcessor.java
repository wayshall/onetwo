package org.onetwo.ext.ons.consumer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.ext.alimq.ConsumContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Lists;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ConsumerProcessor {

	void parse(Map<String, ConsumerMeta> consumers);
	

	public static List<Method> findConsumerBeanMethods(Object bean, Class<? extends Annotation> consumerAnnotation){
		Class<?> targetClass = AopUtils.getTargetClass(bean);
		return findConsumersMethods(targetClass, consumerAnnotation);
	}
	public static List<Method> findConsumersMethods(Class<?> targetClass, Class<? extends Annotation> consumerAnnotation){
		List<Method> consumerMethods = Lists.newArrayList();
		ReflectionUtils.doWithMethods(targetClass, m->consumerMethods.add(m), m->{
			if(AnnotationUtils.findAnnotation(m, consumerAnnotation)!=null){
				Parameter[] parameters = m.getParameters();
				if(parameters.length==0 || parameters.length>2){
					throw new BaseException("the maximum parameter of consumer method[" + m.toGenericString() + "]  is two.");
				}
				if(parameters[0].getType()!=ConsumContext.class){
					throw new BaseException("the first parameter type of the consumer method[" + m.toGenericString() + "] must be: "+ConsumContext.class);
				}
				return true;
			}
			return false;
		});
		return consumerMethods;
	}
}

