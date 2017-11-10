package org.onetwo.ext.ons.consumer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.onetwo.ext.alimq.ConsumContext;
import org.springframework.util.ReflectionUtils;

/**
 * @author wayshall
 * <br/>
 */
public class DelegateCustomONSConsumer implements CustomONSConsumer<Object> {
	private Object target;
	private Method consumerMethod;
	private Parameter[] parameters;
	
	public DelegateCustomONSConsumer(Object target, Method consumerMethod) {
		super();
		this.target = target;
		this.consumerMethod = consumerMethod;
		this.parameters = consumerMethod.getParameters();
	}
	
	@Override
	public void doConsume(ConsumContext consumContext) {
		if(parameters.length==1){
			ReflectionUtils.invokeMethod(consumerMethod, target, consumContext);
		}else if(parameters.length==2){
			ReflectionUtils.invokeMethod(consumerMethod, target, consumContext, consumContext.getDeserializedBody());
		}
	}
	
	
}
