package org.onetwo.guava;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ExpressionFacotry;
import org.slf4j.Logger;

public class JavassistUtils {

	
	private static final Logger logger = JFishLoggerFactory.logger(JavassistUtils.class);
	
	private static final ClassPool classPool = ClassPool.getDefault();
	
	static {
		classPool.insertClassPath(new ClassClassPath(JavassistUtils.class));
	}
	
	private JavassistUtils(){
	}
	
	public static CtClass replacePrivateField(String className, String feildName, Class<?> newFieldType){
		CtClass factoryClass;
		try {
			String statment = ExpressionFacotry.DOLOR.parse("private final ${type} ${feildName} = new ${type}();", 
															"feildName", feildName, "type", newFieldType.getName());
			factoryClass = classPool.getCtClass(className);
			CtConstructor constructor = factoryClass.getDeclaredConstructor(null);
			constructor.insertAfter(statment);
		} catch (Exception e) {
			throw new BaseException("replace initialize field error: " + e.getMessage(), e);
		}
		return factoryClass;
	}
	
	public static Class<?> toCtClass(CtClass ctClass){
		try {
			return ctClass.toClass();
		} catch (Exception e) {
			throw new BaseException("to class error: " + e.getMessage(), e);
		}
	}
	
	
}
