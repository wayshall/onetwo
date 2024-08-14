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
		// 可以查找到 @SemanicRestApiClientMethodTest 上的 @RestApiClientMethodTest 注解，但属性值就只是 @RestApiClientMethodTest 本身但属性值
		Method m = ReflectUtils.findMethod(SemanticAPITest.class, "test");
		RestApiClientMethodTest meta = AnnotationUtils.findAnnotation(m, RestApiClientMethodTest.class);
		assertThat(meta).isNotNull();
		assertThat(meta.url()).contains("test_url");
		assertThat(meta.path()).isEqualTo("default_path");
		assertThat(meta.name()).isEmpty();
		
		// 可以查找到 @SemanicRestApiClientMethodTest 上的 @RestApiClientMethodTest 注解，且会合并@SemanicRestApiClientMethodTest注解的值
		meta = AnnotatedElementUtils.findMergedAnnotation(m, RestApiClientMethodTest.class);
		assertThat(meta.name()).isEqualTo("SemanticAPITestName");
		assertThat(meta.path()).isEqualTo("default_path");
		assertThat(meta.url()).isEqualTo("Semantic_url");
	}
	

	public static class SemanticAPITest {
		@SemanicRestApiClientMethodTest(name="SemanticAPITestName", url="Semantic_url")
		public void test() {
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@RestApiClientMethodTest(url="test_url", path="default_path")
	public static @interface SemanicRestApiClientMethodTest {
		@AliasFor(annotation=RestApiClientMethodTest.class)
		String name();
		@AliasFor(annotation=RestApiClientMethodTest.class)
		String url();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	static public @interface RestApiClientMethodTest {
		
		String name() default "";
		String path() default "";
		String url() default "";
	}

}
