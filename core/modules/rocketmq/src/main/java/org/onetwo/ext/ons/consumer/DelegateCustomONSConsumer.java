package org.onetwo.ext.ons.consumer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.stream.Stream;

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
	private Class<?> messageBodyClass;
	
	public DelegateCustomONSConsumer(Object target, Method consumerMethod) {
		super();
		this.target = target;
		this.consumerMethod = consumerMethod;
		this.parameters = consumerMethod.getParameters();
		this.messageBodyClass = findMessageBodyClass(parameters);
	}
	
	@Override
	public void doConsume(ConsumContext consumContext) {
		if(parameters.length==1){
			ReflectionUtils.invokeMethod(consumerMethod, target, consumContext);
		}else if(parameters.length==2){
			ReflectionUtils.invokeMethod(consumerMethod, target, consumContext, consumContext.getDeserializedBody());
		}
	}
	
	final public Class<?> findMessageBodyClass(Parameter[] parameters) {
		return Stream.of(parameters).filter(p -> {
			return p.getType()!=ConsumContext.class;
		})
		.map(p -> p.getType())
		.findFirst()
		.orElse(null);
	}

	public Class<?> getMessageBodyClass(ConsumContext consumContext) {
		return messageBodyClass;
	}
	
	
}
