package org.onetwo.common.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.validation.annotation.Validated;

public abstract class AbstractMethodResolver<T extends MethodParameter> {
	protected final Method method;
	protected final List<T> parameters;
	protected final Class<?> declaringClass;

	public AbstractMethodResolver(Method method) {
		super();
		this.method = method;
		this.parameters = createMethodParameters(method);
		this.declaringClass = method.getDeclaringClass();
	}
	
	public final Optional<AnnotationAttributes> getAnnotationAttributes(Class<? extends Annotation> annoClass){
		return SpringUtils.getAnnotationAttributes(method, annoClass);
	}
	
	public Class<?> getMethodReturnType() {
		return method.getReturnType();
	}
	
	public boolean isReturnVoid(){
		return getMethodReturnType() == void.class;
	}

	public boolean isGenericReturnType(){
		return (method.getGenericReturnType() instanceof ParameterizedType);
	}

	public Method getMethod() {
		return method;
	}

	public Class<?> getDeclaringClass() {
		return declaringClass;
	}
	
	final public Optional<T> findParameterByType(Class<?> targetParamType){
		return parameters.stream()
					.filter(p->targetParamType.isAssignableFrom(p.getParameterType()))
					.findFirst();
	}
	
	@SuppressWarnings("unchecked")
	final protected <E> Optional<E> findParameterValueByType(Object[] args, Class<E> targetParamType){
		return (Optional<E>)findParameterByType(targetParamType).map(p->{
			return args[p.getParameterIndex()];
		});
	}

	public void validateArgements(ValidatorWrapper validatorWrapper, Object[] args){
		if(validatorWrapper==null){
			return ;
		}
		for(T parameter : parameters){
			Object paramValue = args[parameter.getParameterIndex()];
			if(parameter.hasParameterAnnotation(Validated.class)){
				Validated validated = parameter.getParameterAnnotation(Validated.class);
				validatorWrapper.throwIfValidateFailed(paramValue, validated.value());
			}else if(parameter.hasParameterAnnotation(Valid.class)){
				validatorWrapper.throwIfValidateFailed(paramValue);
			}
		}
	}

	final protected List<T> createMethodParameters(Method method){
		int psize = method.getParameterTypes().length;
		List<T> parameters = LangUtils.newArrayList(psize+2);
//		this.parameterNames = LangUtils.newArrayList(psize);
		T mp = null;
		
		Parameter[] paramters = method.getParameters();
		for(int index=0; index<psize; index++){
			mp = createMethodParameter(method, index, paramters[index]);
			parameters.add(mp);
		}
		return parameters;
	}
	
	public List<T> getParameters() {
		return parameters;
	}
	
	public T getParameter(String name) {
		return parameters.stream().filter( m->( m.getParameterName().equals(name))).findFirst().get();
	}

	abstract protected T createMethodParameter(Method method, int parameterIndex, Parameter parameter);
}
