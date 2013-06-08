package org.onetwo.common;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ReflectUtils;

import test.entity.UserEntity;

public class GenericTypeTest {
	
	private static interface GenericReturnTypeInterface {

		public UserEntity findById(Long id);
		public UserEntity[] findList(String userName);
		public List<UserEntity> findList(Object...params);
		
		
	}
	public static class A {
		protected Logger logger = Logger.getLogger(this.getClass());
		public A(){
			System.out.println("A:"+getClass());
			System.out.println("A:"+logger);
		}
	}
	public static class B extends A {
		public B(){
			System.out.println("B:"+getClass());
			System.out.println("B:"+logger);
		}
	}
	private org.slf4j.Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Test
	public void testMethodGenericReturnType(){
//		A a = new B();
		
		Method[] methods = GenericReturnTypeInterface.class.getMethods();
		for(Method method : methods){
			System.out.println(method);
			Class<?> returnType = method.getReturnType();
			logger.info("returnType {}, type {}", returnType, (returnType instanceof Type));
			Type type = method.getGenericReturnType();
			logger.info("type: " + type);
			if(type instanceof ParameterizedType){
				ParameterizedType ptype = (ParameterizedType) type;
				logger.info("ptype: " + ptype.getActualTypeArguments()[0]);
				
				Assert.assertEquals(List.class, ptype.getRawType());
				Assert.assertEquals(UserEntity.class, ReflectUtils.getGenricType(type, 0));
			}
		}
	}

	public static void main(String[] args){
		List<Timestamp> list = new ArrayList<Timestamp>();  
	    Date date = new Date();  
	    Timestamp time = new Timestamp(date.getTime());  
	    upperBound2(list,time);  
	}
	

	public static <T extends Date> void upperBound2(List<T> list, T date)  
	{  
	    list.add(date);  
	} 
}
