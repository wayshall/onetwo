package org.onetwo.common.fish.richmodel;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.ReflectUtils;

public class JavassistTest {
	
	@Test
	public void testClass() throws Exception{
		CtClass ctclass = ClassPool.getDefault().get("org.onetwo.common.fish.richmodel.UserModel");
//		ctclass.setName("org.onetwo.common.fish.richmodel.UserModel$impl");
        CtMethod entityMethod = CtMethod.make("public static Class getEntityClass(){ return org.onetwo.common.fish.richmodel.UserModel.class; }", ctclass);
        ctclass.addMethod(entityMethod);
        ctclass.defrost();
        
        ctclass.toClass();
        
        System.out.println("clsName: " + UserModel.class.getName());
        Class<?> cls1 = ReflectUtils.loadClass("org.onetwo.common.fish.richmodel.UserModel");
        Class cls2 = UserModel.class;
        Assert.assertEquals(cls1, cls2);
//        UserModel user = (UserModel)ctclass.toClass().newInstance();
        Assert.assertEquals(UserModel.class, UserModel.getEntityClass());
	}

}
