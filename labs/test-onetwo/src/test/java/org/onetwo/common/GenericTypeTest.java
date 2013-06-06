package org.onetwo.common;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.ReflectUtils;

import test.entity.UserEntity;

public class GenericTypeTest {
	
	private static interface GenericReturnTypeInterface {

		public UserEntity findById(Long id);
		public List<UserEntity> findList(Object...params);
		
		
	}
	
	@Test
	public void testMethodGenericReturnType(){
		Method[] methods = GenericReturnTypeInterface.class.getMethods();
		for(Method method : methods){
			System.out.println(method);
//			Class<?> returnType = method.getReturnType();
			Type type = method.getGenericReturnType();
			if(type instanceof ParameterizedType){
				ParameterizedType ptype = (ParameterizedType) type;
				System.out.println("ptype: " + ptype.getActualTypeArguments()[0]);
				
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
