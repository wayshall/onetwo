package org.onetwo.ext.ons.consumer;

import java.lang.reflect.Method;

import org.onetwo.ext.alimq.ConsumContext;
import org.springframework.util.ReflectionUtils;

/**
 * @author wayshall
 * <br/>
 */
public class DelegateCustomONSConsumer implements CustomONSConsumer<Object> {
	private Object target;
	private Method consumerMethod;
	public DelegateCustomONSConsumer(Object target, Method consumerMethod) {
		super();
		this.target = target;
		this.consumerMethod = consumerMethod;
	}
	
	@Override
	public void doConsume(ConsumContext consumContext) {
		ReflectionUtils.invokeMethod(consumerMethod, target, consumContext);
	}
	
}
