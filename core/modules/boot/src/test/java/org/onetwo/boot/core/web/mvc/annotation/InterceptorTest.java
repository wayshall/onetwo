package org.onetwo.boot.core.web.mvc.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.onetwo.boot.core.web.mvc.annotation.Interceptor.Interceptors;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.Child2MvcInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.CombinationMVCInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.Combine1MvcInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.InterceptableChild1;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.InterceptableCombine1;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.InterceptableCombine2;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.InterceptableParent;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.ParentMvcInterceptor;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * @author wayshall
 * <br/>
 */
public class InterceptorTest {

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
	private Object hm;
	
	@Test
	public void testParent(){
		Interceptor inter = InterceptableParent.class.getAnnotation(Interceptor.class);
		assertThat(inter).isNotNull();
	}
	
	@Test
	public void testChild1(){
		Interceptor inter = InterceptableChild1.class.getAnnotation(Interceptor.class);
		assertThat(inter).isNotNull();
		
		Annotation[] annos = InterceptableChild1.class.getAnnotations();
		boolean anyMatch = Stream.of(annos).anyMatch(anno->Interceptor.class.isInstance(anno));
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
			boolean res = anno.annotationType().isAnnotationPresent(Interceptors.class);
			return res;
		})
		.collect(Collectors.toList());
		assertThat(inters).isNotEmpty();
		Annotation combina = inters.get(0);
		assertThat(combina).isInstanceOf(CombinationMVCInterceptor.class);
		
		//查找包含了@Interceptors注解的注解
		List<Annotation> combines = AnnotationUtils.findCombineAnnotations(InterceptableCombine1.class, Interceptors.class);
		assertThat(combines).isNotEmpty();
		
		//combines.get(0).annotationType() = @CombinationMVCInterceptor
		Class<?> combinationMVCInterceptorClass = combines.get(0).annotationType();
		//@CombinationMVCInterceptor注解虽然包含了@Interceptor注解，但是因为是可重复的，直接查找@Interceptor是找不到的
		AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(combinationMVCInterceptorClass, Interceptor.class);
		assertThat(attrs).isNull();

		//@CombinationMVCInterceptor注解因为是可重复的，必须要查找包装单个@Interceptor的@Interceptors 才能找到
		attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(combinationMVCInterceptorClass, Interceptors.class);
		assertThat(attrs).isNotNull();
		System.out.println("attrs:"+attrs);
		
		//通过getMergedRepeatableAnnotations方法直接在@CombinationMVCInterceptor上查找@Interceptor
		Set<Interceptor> repeatableInters = AnnotatedElementUtils.getMergedRepeatableAnnotations(combinationMVCInterceptorClass, Interceptor.class);
		assertThat(repeatableInters).isNotEmpty();
		
		Set<Interceptor> repeatableInters2 = AnnotatedElementUtils.getMergedRepeatableAnnotations(combinationMVCInterceptorClass, Interceptor.class, Interceptors.class);
		assertThat(repeatableInters2).isNotEmpty();
		assertThat(repeatableInters2).isEqualTo(repeatableInters);

		//其实还可以直接在使用了@CombinationMVCInterceptor注解的类上面直接查找@Interceptor
		//但是会包含合并了的父类的注解，前提是子类和父类使用了不同的注解，如InterceptableCombine1类使用了包含@Interceptor的组合注解，而父类直接使用了@Interceptor注解
		repeatableInters = AnnotatedElementUtils.getMergedRepeatableAnnotations(InterceptableCombine1.class, Interceptor.class);
		assertThat(repeatableInters).isNotEmpty();
		assertThat(repeatableInters).isNotEqualTo(repeatableInters2);
		//包含父类的注解
		boolean matchParentClassInter = repeatableInters.stream().anyMatch(inter->inter.value()==ParentMvcInterceptor.class);
		assertThat(matchParentClassInter).isTrue();
		

		repeatableInters = AnnotatedElementUtils.getMergedRepeatableAnnotations(InterceptableCombine2.class, Interceptor.class);
		System.out.println("InterceptableCombine2 inters["+repeatableInters.size()+"]:" + repeatableInters);
		assertThat(repeatableInters).size().isEqualTo(1);
		Interceptor inter = LangUtils.getFirst(repeatableInters);
		assertThat(inter.value()).isEqualTo(Child2MvcInterceptor.class);
		
		//虽然子类和父类都有@Interceptor注解，但是子类的会覆盖父类的，不会合并
		Method testMethod = ReflectUtils.findMethod(InterceptableCombine2.class, "test1");
		System.out.println("testMethod:"+testMethod);
		//不会查找和合并父类对应的test方法注解
		repeatableInters = AnnotatedElementUtils.getMergedRepeatableAnnotations(testMethod, Interceptor.class);
		System.out.println("testMethod inters["+repeatableInters.size()+"]:" + repeatableInters);
		assertThat(repeatableInters).size().isEqualTo(2);
		inter = LangUtils.getFirst(repeatableInters);
		assertThat(inter.value()).isEqualTo(Combine1MvcInterceptor.class);
	}

}
