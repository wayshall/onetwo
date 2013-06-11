package org.example.app.model.member;

import java.lang.reflect.Method;

import org.example.app.model.member.dao.UserDao;
import org.junit.Test;
import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;

public class UserDaoParameterNameTest {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
	
	@Test
	public void testGetMethodNamesBySpring(){
		Method[] methods = UserDao.class.getDeclaredMethods();
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
	
}
