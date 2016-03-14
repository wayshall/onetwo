package org.onetwo.common.delegate;

import java.lang.reflect.Method;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.ArrayUtils;

@SuppressWarnings("rawtypes")
public class DelegateMethodImpl implements DelegateMethod {

	
	private Object target;
	private String methodName;
	private Method method;

//	private Object returnValue;
	
	public DelegateMethodImpl(Object delegate, String methodName, Class...argTypes){
		this.target = delegate;
		this.methodName = methodName;
		if(ArrayUtils.hasElement(argTypes)){
			this.method = findMethod(argTypes);
		}
//		this.returnValue = null;
	}
	
	private Class getDelegateClass(){
		return (target instanceof Class)?(Class)target:target.getClass();
	}
	
	@Override
	public Object invoke(Object...args){
		Object returnValue = null;
		if(method==null){
			Class[] argTypes = ReflectUtils.findTypes(args);
			method = findMethod(argTypes);
		}
//			returnValue = this.method.invoke(target, args);
		returnValue = ReflectUtils.invokeMethod(method, target, args);
		
		return returnValue;
	}
	
	@Override
	public Object getTarget() {
		return target;
	}
	
	private Method findMethod(Class...argTypes){
		Method fmethod = ReflectUtils.findMethod(getDelegateClass(), methodName, argTypes);
		return fmethod;
	}

	public Method getMethod() {
		return method;
	}
/*
	public Object getReturnValue() {
		return returnValue;
	}

	void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}*/

	public String getMethodName() {
		return methodName;
	}
}
