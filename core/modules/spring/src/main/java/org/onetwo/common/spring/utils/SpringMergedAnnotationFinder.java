package org.onetwo.common.spring.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.onetwo.common.annotation.AnnotationInfo.AnnotationFinder;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * @author wayshall
 * <br/>
 */
public class SpringMergedAnnotationFinder implements AnnotationFinder {
	
	final public static SpringMergedAnnotationFinder INSTANCE = new SpringMergedAnnotationFinder();

	@Override
	public <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
//		return AnnotationUtils.findAnnotation(annotatedElement, annotationType);
		return AnnotatedElementUtils.getMergedAnnotation(annotatedElement, annotationType);
	}

}
