package org.onetwo.javassist;

import java.lang.reflect.Method;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.LocalVariableAttribute;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.TestController;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.dq.MethodBuilder;
import org.slf4j.Logger;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;

import test.entity.UserEntity;

public class JavassistTest {

	private static final String PROXY_POSTFIX = "$proxy";
	private static interface UserDaoTest {
		public void findPage(Page<Object> page);
		public UserEntity findById(Long id);
	}

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	ClassPool classPool = ClassPool.getDefault();
	ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
	
	@Before
	public void setup(){
		classPool.insertClassPath(new ClassClassPath(this.getClass()));
	}
	
//	@Test
	public void testGetMethodNamesBySpring(){
		Method[] methods = TestController.class.getDeclaredMethods();
		for(Method method : methods){
			MyLoggerFactory.getLogger(this.getClass()).info(method.toGenericString());
			logger.info("method: " + method);
			int index = 0;
			for(Class<?> ptype : method.getParameterTypes()){
				MethodParameter mp = new MethodParameter(method, index);
				mp.initParameterNameDiscovery(pnd);
				System.out.println("===>"+method.getName()+": "+mp.getParameterName());
			}
		}
	}
	
//	@Test
	public void testGetMethodNamesByJavassist() throws Exception{
		CtClass ctclass = this.classPool.getCtClass(TestController.class.getName());
		Method[] methods = TestController.class.getDeclaredMethods();
		for(Method method : methods){
			MyLoggerFactory.getLogger(this.getClass()).info(method.toGenericString());
			logger.info("method: " + method);
			CtMethod ctMethod = ctclass.getDeclaredMethod(method.getName());
			LocalVariableAttribute attrInfo = (LocalVariableAttribute) ctMethod.getMethodInfo().getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
			
			MethodBuilder mb = MethodBuilder.newPublicMethod()._return(method.getGenericReturnType()).name(method.getName());
			int index = 0;
			for(Class<?> ptype : method.getParameterTypes()){
				MethodParameter mp = new MethodParameter(method, index);
				System.out.println("method name: "+attrInfo.variableName(1));
//				mb.arg("arg"+(index++), ptype);
				mb.arg(mp.getParameterName(), ptype);
				index++;
			}
			mb.body("return null;");
			System.out.println("mb:"+mb);
		}
	}

	
	@Test
	public void testMakeClass() throws Exception{
		CtClass intefaceCtClass = this.classPool.getCtClass(TestInterface.class.getName());
		CtClass ctclass = this.classPool.makeClass(TestController.class.getName()+PROXY_POSTFIX);
		ctclass.addInterface(intefaceCtClass);
		for(CtMethod method : intefaceCtClass.getDeclaredMethods()){
			logger.info("method: " + method);
			CtMethod ctMethod = new CtMethod(method.getReturnType(), method.getName(), method.getParameterTypes(), ctclass);
			
			String body = LangUtils.append("{System.out.println(\"test\");",
								"return null;}");
			ctMethod.setBody(body);
			
			ctclass.addMethod(ctMethod);
		}
		TestInterface obj = (TestInterface)ctclass.toClass().newInstance();
		obj.test1("test");
	}
}
