package org.onetwo.javassist;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
import org.onetwo.common.utils.SimpleBlock;
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
	
	@Test
	public void testGetMethodNamesBySpring(){
		//TestInterface
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
//			int index = 0;
			for(Class<?> ptype : method.getParameterTypes()){
				System.out.println("method name: "+attrInfo.variableName(1));
//				mb.arg("arg"+(index++), ptype);
				mb.arg(attrInfo.variableName(1), ptype);
//				index++;
			}
			mb.body("return null;");
			System.out.println("mb:"+mb);
		}
	}
	
	public static class JavassistJDynamicProxy {
		private static final String PROXY_POSTFIX = "$proxy";
		private ClassPool classPool = ClassPool.getDefault();
		private CtClass implCtClass;
		private List<Method> proxyMethods = new ArrayList<Method>();
		
		public JavassistJDynamicProxy(Class<?>...proxiedInterfaces){
			for (int i = 0; i < proxiedInterfaces.length; i++) {
				Class<?> proxiedInterface = proxiedInterfaces[i];
				Method[] methods = proxiedInterface.getDeclaredMethods();
				for (int j = 0; j < methods.length; j++) {
					Method method = methods[j];
					proxyMethods.add(method);
				}
			}
			implCtClass = this.classPool.makeClass(TestInterface.class.getName()+PROXY_POSTFIX);
		}
	}
	


//	@Test
	public void testRawMakeClass() throws Exception{
		CtClass intefaceCtClass = this.classPool.getCtClass(TestInterface.class.getName());
		CtClass ctclass = this.classPool.makeClass(TestInterface.class.getName()+PROXY_POSTFIX);
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

	JavassistProxyFacotory facotry = new JavassistProxyFacotory();
	@Test
	public void testProxyFactoryMakeClass() throws Exception{
		//返回所有内部类？
		System.out.println("class: " + LangUtils.toString(this.getClass().getDeclaredClasses()));
		
		TestInterface obj = (TestInterface)facotry.createProxy(new String[]{TestInterface.class.getName()}, new SimpleBlock<CtMethod, String>() {
			
			@Override
			public String execute(CtMethod object) {
				return LangUtils.append("{System.out.println($0.getClass().getName()+\"-\" + $1);",
										"return null;}");
			}
		});
		obj.test1("test");
	}
}
