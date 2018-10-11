package ThrowableAnalyzerDelegate;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author wayshall
 * <br/>
 */
abstract public class MethodInterceptorAdaptor<T> implements MethodInterceptor {

	final private String[] interceptMethodNames;
	
	public MethodInterceptorAdaptor(String...interceptMethodNames){
		this.interceptMethodNames = interceptMethodNames;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		T thisObject = (T)invocation.getThis();
		if(isInterceptMethod(thisObject, invocation.getMethod())){
			return handleInterceptMethod(thisObject, invocation);
		}
		return invokeTarget(invocation);
	}

	protected boolean isInterceptMethod(T target, Method method){
		return ArrayUtils.contains(interceptMethodNames, method.getName());
	}
	abstract protected Object handleInterceptMethod(T target, MethodInvocation invocation);

	protected Object invokeTarget(MethodInvocation invocation){
		return ReflectionUtils.invokeMethod(invocation.getMethod(), invocation.getThis(), invocation.getArguments());
	}

}
