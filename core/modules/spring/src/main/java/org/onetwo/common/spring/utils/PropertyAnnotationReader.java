package org.onetwo.common.spring.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Builder;
import lombok.Value;

import org.onetwo.common.spring.annotation.Property;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.annotation.AnnotationAttributes;

/**
 * @author wayshall
 * <br/>
 */
final public class PropertyAnnotationReader {
	
	public static final PropertyAnnotationReader INSTANCE = new PropertyAnnotationReader();
	
//	private Cache<Class<? extends Annotation>, List<PropertyMeta>> interceptorMetaCaches = CacheBuilder.newBuilder().build();
	
	public List<PropertyAnnoMeta> readProperties(AnnotationAttributes parentAnnotation){
		return readProperties(parentAnnotation, "properties");
	}
	public List<PropertyAnnoMeta> readProperties(AnnotationAttributes parentAnnotation, String propertyName){
		Property[] properties = parentAnnotation.getAnnotationArray(propertyName, Property.class);
		if(LangUtils.isEmpty(properties)){
			return Collections.emptyList();
		}
		List<PropertyAnnoMeta> propertyMetas = Stream.of(properties)
												.map(prop->new PropertyAnnoMeta(prop.name(), prop.value()))
												.collect(Collectors.toList());
		return propertyMetas;
	}

	@Value
	@Builder
	public static class PropertyAnnoMeta {
//		Class<?> targetClass;
		String name;
		String value;
	}
	
	private PropertyAnnotationReader(){
	}
}
