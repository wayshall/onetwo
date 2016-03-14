package org.onetwo.common.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;


@SuppressWarnings("unchecked")
public class AnnotationInfo {

	private final Class<?> sourceClass;
	private final List<Annotation> annotations;
	

	public AnnotationInfo(Class<?> sourceClass) {
		this(sourceClass, sourceClass.getAnnotations());
	}
	
	public AnnotationInfo(Class<?> sourceClass, Annotation...annoArray) {
		this.sourceClass = sourceClass;
		if(LangUtils.isEmpty(annoArray)){
			this.annotations = Collections.EMPTY_LIST;
			return ;
		}
		this.annotations = new ArrayList<Annotation>(annoArray.length);
		for(Annotation a : annoArray)
			annotations.add(a);
	}


	public Class<?> getSourceClass() {
		return sourceClass;
	}


	public List<Annotation> getAnnotations() {
		return annotations;
	}
	
	public boolean hasAnnotation(Class<? extends Annotation> annoClass){
		return getAnnotation(annoClass)!=null;
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> annoClass){
		Assert.notNull(annoClass, "annotation class can not be null");
		for(Annotation anno : getAnnotations()){
			if(annoClass.isInstance(anno))
				return (T)anno;
		}
		return null;
	}
}
