package org.onetwo.common.spring.context;

import java.lang.annotation.Annotation;
import java.util.List;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractImportSelector<A extends Annotation> implements ImportSelector {
	
	public static AnnotationAttributes getAnnotationAttributes(AnnotationMetadata metadata, Class<?> annoType){
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annoType.getName(), false));
		return attributes;
	}
	
	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		Class<?> annoType = GenericTypeResolver.resolveTypeArgument(getClass(), AbstractImportSelector.class);
		//support @AliasFor
		AnnotationAttributes attributes = getAnnotationAttributes(metadata, annoType);
		if (attributes == null) {
			throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", annoType.getSimpleName(), metadata.getClassName()));
		}
		List<String> imports = doSelect(metadata, attributes);
		return imports.toArray(new String[0]);
	}
	
	abstract protected List<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes);
	

}
