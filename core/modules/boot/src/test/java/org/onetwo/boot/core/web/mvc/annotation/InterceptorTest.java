package org.onetwo.boot.core.web.mvc.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.onetwo.boot.core.web.mvc.annotation.Interceptor.Interceptors;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.CombinationMVCInterceptor;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.InterceptableChild1;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.InterceptableCombine1;
import org.onetwo.boot.core.web.mvc.annotation.TestInterceptors.InterceptableParent;
import org.onetwo.common.annotation.AnnotationUtils;
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
		
		List<Annotation> combines = AnnotationUtils.findCombineAnnotations(InterceptableCombine1.class, Interceptors.class);
		assertThat(combines).isNotEmpty();
		
		//combines.get(0).annotationType() = @CombinationMVCInterceptor
		Class<?> combinationMVCInterceptorClass = combines.get(0).annotationType();
		AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(combinationMVCInterceptorClass, Interceptor.class);
		assertThat(attrs).isNull();

		attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(combinationMVCInterceptorClass, Interceptors.class);
		assertThat(attrs).isNotNull();
		System.out.println("attrs:"+attrs);
		
		Set<Interceptor> repeatableInters = AnnotatedElementUtils.getMergedRepeatableAnnotations(combinationMVCInterceptorClass, Interceptor.class);
		assertThat(repeatableInters).isNotEmpty();
		
		Set<Interceptor> repeatableInters2 = AnnotatedElementUtils.getMergedRepeatableAnnotations(combinationMVCInterceptorClass, Interceptor.class, Interceptors.class);
		assertThat(repeatableInters2).isNotEmpty();
		assertThat(repeatableInters2).isEqualTo(repeatableInters2);

		repeatableInters = AnnotatedElementUtils.getMergedRepeatableAnnotations(InterceptableCombine1.class, Interceptor.class);
		assertThat(repeatableInters).isNotEmpty();
	}

}
