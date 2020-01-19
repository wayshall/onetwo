package org.onetwo.common.spring;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.Test;
import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

public class AnnotationUtilsTest {

	@Test
	public void testSemantic() {
		Method m = ReflectUtils.findMethod(SemanticAPITest.class, "test");
		RestApiClientMethodTest meta = AnnotationUtils.findAnnotation(m, RestApiClientMethodTest.class);
		assertThat(meta).isNotNull();
		assertThat(meta.url()).contains("test_url");
		assertThat(meta.name()).isEmpty();
		

		meta = AnnotatedElementUtils.findMergedAnnotation(m, RestApiClientMethodTest.class);
		assertThat(meta.name()).isEqualTo("SemanticAPITestName");
	}
	

	public static class SemanticAPITest {
		@SemanicRestApiClientMethodTest(name="SemanticAPITestName")
		public void test() {
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@RestApiClientMethodTest(url="test_url")
	public static @interface SemanicRestApiClientMethodTest {
		@AliasFor(annotation=RestApiClientMethodTest.class)
		String name();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	static public @interface RestApiClientMethodTest {
		
		String name() default "";
		String path() default "";
		String url() default "";
	}

}
