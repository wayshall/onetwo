package org.onetwo.common.spring.context;

import java.util.Collection;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
abstract public class BaseImportSelector implements ImportSelector {
	
	protected Class<?> annotationClass;

	public BaseImportSelector() {
	}
	
	public BaseImportSelector(Class<?> annotationClass) {
		super();
		this.annotationClass = annotationClass;
	}

	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		/*Class<?> annoType = GenericTypeResolver.resolveTypeArgument(getClass(), AbstractImportSelector.class);
		//support @AliasFor
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(metadata, annoType);*/
		AnnotationAttributes attributes = getAnnotationAttributes(metadata);
		if (attributes == null) {
			throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", annotationClass.getSimpleName(), metadata.getClassName()));
		}
		Collection<String> imports = doSelect(metadata, attributes);
		return imports.toArray(new String[0]);
	}
	
	abstract protected Collection<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes);
	

	protected AnnotationAttributes getAnnotationAttributes(AnnotationMetadata metadata){
		//support @AliasFor
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(metadata, annotationClass);
		return attributes;
	}

	final public void setAnnotationClass(Class<?> annotationClass) {
		this.annotationClass = annotationClass;
	}
	
}
