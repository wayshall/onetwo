package org.onetwo.guava;

import javassist.CtClass;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.ReflectUtils;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.MyAnnotatedHandlerFinder;

public class ReplaceHandlerFindingStrategyTest {
	
	
	@Test
	public void test(){
		CtClass cls = JavassistUtils.replacePrivateField("com.google.common.eventbus.EventBus", "finder", MyAnnotatedHandlerFinder.class);
		JavassistUtils.toCtClass(cls);
		EventBus eventBus = new EventBus();
		Object finder = ReflectUtils.getFieldValue(eventBus, "finder");
		Assert.assertTrue(MyAnnotatedHandlerFinder.class.isInstance(finder));
	}

}
