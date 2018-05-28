package org.onetwo.boot.core.web.mvc.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.onetwo.boot.core.web.mvc.annotation.InterceptorNorepeatTest.TestInterceptors.CombinationMVCInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.InterceptorNorepeatTest.TestInterceptors.Combine1MvcInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.InterceptorNorepeatTest.TestInterceptors.Combine2MvcInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.InterceptorNorepeatTest.TestInterceptors.InterceptableChild1;
import org.onetwo.boot.core.web.mvc.annotation.InterceptorNorepeatTest.TestInterceptors.InterceptableCombine1;
import org.onetwo.boot.core.web.mvc.annotation.InterceptorNorepeatTest.TestInterceptors.InterceptableCombine2;
import org.onetwo.boot.core.web.mvc.annotation.InterceptorNorepeatTest.TestInterceptors.InterceptableParent;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptor;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.spring.annotation.Property;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * @author wayshall
 * <br/>
 */
public class InterceptorNorepeatTest {

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
	private Object hm;
	
	@Test
	public void testParent(){
		InterceptorTest inter = InterceptableParent.class.getAnnotation(InterceptorTest.class);
		assertThat(inter).isNotNull();
	}
	
	@Test
	public void testChild1(){
		InterceptorTest inter = InterceptableChild1.class.getAnnotation(InterceptorTest.class);
		assertThat(inter).isNotNull();
		
		Annotation[] annos = InterceptableChild1.class.getAnnotations();
		boolean anyMatch = Stream.of(annos).anyMatch(anno->InterceptorTest.class.isInstance(anno));
		assertThat(anyMatch).isTrue();
	}
	
	@Test
	public void testInterceptableCombine1(){
		Annotation[] annos = InterceptableCombine1.class.getAnnotations();
		List<Annotation> inters = Stream.of(annos).filter(anno->{
			/*String clsName = anno.annotationType().getName();
			MetadataReader meta;
			try {
				meta = metadataReaderFactory.getMetadataReader(clsName);
			} catch (Exception e) {
				throw new BaseException("error", e);
			}
			boolean annoted = meta.getAnnotationMetadata().isAnnotated(Interceptor.class.getName());*/
//			Interceptor inter = anno.annotationType().getAnnotation(Interceptor.class);
			boolean res = anno.annotationType().isAnnotationPresent(InterceptorTest.class);
			return res;
		})
		.collect(Collectors.toList());
		assertThat(inters).isNotEmpty();
		Annotation combina = inters.get(0);
		assertThat(combina).isInstanceOf(CombinationMVCInterceptor.class);
		
		//查找包含了@Interceptor注解的注解
		List<Annotation> combines = AnnotationUtils.findCombineAnnotations(InterceptableCombine1.class, InterceptorTest.class);
		assertThat(combines).isNotEmpty();
		
		//combines.get(0).annotationType() = @CombinationMVCInterceptor
		Class<?> combinationMVCInterceptorClass = combines.get(0).annotationType();
		assertThat(combinationMVCInterceptorClass).isEqualTo(CombinationMVCInterceptor.class);
		//@CombinationMVCInterceptor注解虽然包含了@Interceptor注解，但是因为是可重复的，直接查找@Interceptor是找不到的
		AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(combinationMVCInterceptorClass, InterceptorTest.class);
		assertThat(attrs).isNotNull();
		
		

		//其实还可以直接在使用了@CombinationMVCInterceptor注解的类上面直接查找@Interceptor
		//但是会包含合并了的父类的注解，前提是子类和父类使用了不同的注解，如InterceptableCombine1类使用了包含@Interceptor的组合注解，而父类直接使用了@Interceptor注解
		attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(InterceptableCombine1.class, InterceptorTest.class);
		assertThat(attrs).isNotNull();
		assertThat(attrs.getClass("value")).isEqualTo(Combine1MvcInterceptor.class);
		
		//根据注解先后顺序只获取到第一个。。。
		attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(InterceptableCombine2.class, InterceptorTest.class);
		assertThat(attrs).isNotNull();
		assertThat(attrs.getClass("value")).isEqualTo(Combine2MvcInterceptor.class);
	}
	

	static protected class TestInterceptors {
	
		@InterceptorTest(ParentMvcInterceptor.class)
		protected static class InterceptableParent {
			@InterceptorTest(ParentMethodMvcInterceptor.class)
			public void test1(){
			}
		}
	
		protected static class InterceptableChild1 extends InterceptableParent {
			public void test1(){
			}
		}
	
		@InterceptorTest(Child2MvcInterceptor.class)
		protected static class InterceptableChild2 extends InterceptableParent {
			public void test1(){
			}
		}
	
		protected static class InterceptableChild3 extends InterceptableParent {
			@InterceptorTest(Child3MvcInterceptor.class)
			public void test1(){
			}
		}
		
		protected static class InterceptableParentNoAnno {
			public void test1(){
			}
		}
		
		@CombinationMVCInterceptor
		protected static class InterceptableCombine1 extends InterceptableParent {
			public void test1(){
			}
		}
		
		@CombinationMVCInterceptor2
		@CombinationMVCInterceptor
		protected static class InterceptableCombine2 extends InterceptableParent {
			public void test1(){
			}
		}
	
		@InterceptorTest(Child2MvcInterceptor.class)
		protected static class InterceptableCombine3 extends InterceptableParent {
			@CombinationMVCInterceptor
			public void test1(){
			}
		}
	
		protected static class ParentMvcInterceptor extends MvcInterceptorAdapter{
		}
		protected static class ParentMethodMvcInterceptor extends MvcInterceptorAdapter{
		}
		protected static class Child2MvcInterceptor extends MvcInterceptorAdapter{
		}
		protected static class Child3MvcInterceptor extends MvcInterceptorAdapter{
		}
		protected static class Combine1MvcInterceptor extends MvcInterceptorAdapter{
		}
		protected static class Combine2MvcInterceptor extends MvcInterceptorAdapter{
		}
		protected static class Combine2MethodMvcInterceptor extends MvcInterceptorAdapter{
		}
		

		@Target({ElementType.TYPE, ElementType.METHOD})
		@Retention(RetentionPolicy.RUNTIME)
		@Inherited
		@InterceptorTest(Combine1MvcInterceptor.class)
		protected static @interface CombinationMVCInterceptor {
		}
		@Target({ElementType.TYPE, ElementType.METHOD})
		@Retention(RetentionPolicy.RUNTIME)
		@Inherited
		@InterceptorTest(Combine2MvcInterceptor.class)
		protected static @interface CombinationMVCInterceptor2 {
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
//	@Repeatable(InterceptorTests.class)
	static protected @interface InterceptorTest {

		Class<? extends MvcInterceptor> value();
		
		/***
		 * 是否总是创建新实例
		 * 若设置为false，并且properties属性为空，则会从spring的applicationContext里查找，没有找到则创建并根据元数据属性缓存
		 * @author wayshall
		 * @return
		 */
		boolean alwaysCreate() default false;
		
		Property[] properties() default {};
		
	}
}
