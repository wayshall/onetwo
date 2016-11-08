package org.onetwo.boot.plugin;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.boot.plugin.AnnotationUtilsTest.AnnotationTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;

@AnnotationTest("test")
public class AnnotationUtilsTest {
	
	@Test
	public void test(){
		AnnotationTest inst = AnnotationUtils.getAnnotation(AnnotationUtilsTest.class, AnnotationTest.class);
		Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(inst);
		System.out.println("attrs:"+attrs);
		Assert.assertEquals("test", attrs.get("name"));
	}

	@Target({TYPE, METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Controller
	static public @interface AnnotationTest {

		@AliasFor("name")
		String value() default "";
		@AliasFor("value")
		String name() default "";

	}
}
