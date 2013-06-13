package org.onetwo.javassist;

import java.util.concurrent.atomic.AtomicInteger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.SimpleBlock;

public class JavassistProxyFacotory {

	private static final String PROXY_POSTFIX = "jproxy";
	private AtomicInteger count = new AtomicInteger(0);
	
	public Object createProxy(String[] proxiedInterfaces, SimpleBlock<CtMethod, String> func){
		Assert.notEmpty(proxiedInterfaces);
		
		ClassPool classPool = ClassPool.getDefault();
//		List<CtMethod> proxyMethods = new ArrayList<CtMethod>();
		CtClass implCtClass = classPool.makeClass(this.getClass().getName()+PROXY_POSTFIX+count.getAndIncrement());
		try {

			for (int i = 0; i < proxiedInterfaces.length; i++) {
				CtClass proxiedInterface = classPool.getCtClass(proxiedInterfaces[i]);
				implCtClass.addInterface(proxiedInterface);
				
				CtClass intefaceCtClass = classPool.getCtClass(proxiedInterface.getName());
				CtMethod[] methods = intefaceCtClass.getDeclaredMethods();
				
				for (int j = 0; j < methods.length; j++) {
					CtMethod method = methods[j];
//					proxyMethods.add(method);
					String body = func.execute(method);
					CtMethod implMethod = new CtMethod(method.getReturnType(), method.getName(), method.getParameterTypes(), implCtClass);
					implMethod.setBody(body);
					implCtClass.addMethod(implMethod);
				}
			}
			
			return implCtClass.toClass().newInstance();
		} catch (Exception e) {
			throw new BaseException("create proxy error : " + e.getMessage(), e);
		} 
	}
}
