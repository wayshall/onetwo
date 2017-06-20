package org.onetwo.common.annotation;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;


@SuppressWarnings("unchecked")
public class AnnotationInfo {

	private final Class<?> sourceClass;
//	private final List<Annotation> annotations;
	private final Map<Class<? extends Annotation>, Annotation> annotationMap;
	

	public AnnotationInfo(Class<?> sourceClass) {
		this(sourceClass, sourceClass.getAnnotations());
	}
	
	public AnnotationInfo(Class<?> sourceClass, Annotation...annoArray) {
		this.sourceClass = sourceClass;
		if(LangUtils.isEmpty(annoArray)){
//			this.annotations = Collections.EMPTY_LIST;
			this.annotationMap = Collections.emptyMap();
		}else{
//			this.annotations = new ArrayList<Annotation>(annoArray.length);
			this.annotationMap = Maps.newHashMap();
			for(Annotation a : annoArray){
//				annotations.add(a);
				annotationMap.put(a.annotationType(), a);
			}
		}
	}


	public Class<?> getSourceClass() {
		return sourceClass;
	}


	public List<Annotation> getAnnotations() {
		return ImmutableList.copyOf(annotationMap.values());
	}
	
	public boolean hasAnnotation(Class<? extends Annotation> annoClass){
		return getAnnotation(annoClass)!=null;
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> annoClass){
		Assert.notNull(annoClass, "annotation class can not be null");
		/*for(Annotation anno : getAnnotations()){
			if(annoClass.isInstance(anno))
				return (T)anno;
		}
		return null;*/
		return (T)this.annotationMap.get(annoClass);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotationMap == null) ? 0 : annotationMap.hashCode());
		result = prime * result
				+ ((sourceClass == null) ? 0 : sourceClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnotationInfo other = (AnnotationInfo) obj;
		if (annotationMap == null) {
			if (other.annotationMap != null)
				return false;
		} else if (!annotationMap.equals(other.annotationMap))
			return false;
		if (sourceClass == null) {
			if (other.sourceClass != null)
				return false;
		} else if (!sourceClass.equals(other.sourceClass))
			return false;
		return true;
	}
}
