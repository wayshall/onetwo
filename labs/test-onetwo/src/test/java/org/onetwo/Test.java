package org.onetwo;

import java.lang.reflect.Method;

import org.onetwo.common.utils.ReflectUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import test.entity.UserEntity;



public class Test {
	
	public static void main(String[] args){
		Method m = ReflectUtils.findMethod(UserEntity.class, "getUserName");
		System.out.println("m:"+m);
		System.out.println("m:"+m.toGenericString());
	}
	
}
