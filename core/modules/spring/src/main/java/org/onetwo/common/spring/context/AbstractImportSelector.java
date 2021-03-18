package org.onetwo.common.spring.context;

import java.lang.annotation.Annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.GenericTypeResolver;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractImportSelector<A extends Annotation> extends BaseImportSelector implements ImportSelector {
	
//	protected Class<?> annotationClass;
	
	public AbstractImportSelector() {
		super();
		Class<?> annotationClass = GenericTypeResolver.resolveTypeArgument(getClass(), AbstractImportSelector.class);
		setAnnotationClass(annotationClass);
	}
	
}
