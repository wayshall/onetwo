package org.onetwo.common.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

import org.onetwo.common.utils.Assert;

import com.google.common.collect.ImmutableList;


public class AnnotationInfo {
	
	public static interface AnnotationFinder {
		public Annotation getAnnotation(AnnotatedElement annotatedElement, Class<? extends Annotation> annoClass);
	}

	private final Class<?> sourceClass;
//	private final List<Annotation> annotations;
	private final AnnotatedElement annotatedElement;
//	private final Map<Class<? extends Annotation>, Annotation> annotationMap;
	private AnnotationFinder annotationFinder;
	

	public AnnotationInfo(Class<?> sourceClass) {
		this(sourceClass, sourceClass);
	}
	public AnnotationInfo(Class<?> sourceClass, AnnotatedElement annotatedElement) {
//		this(sourceClass, sourceClass.getAnnotations());
		this(sourceClass, annotatedElement, null);
	}
	
	public AnnotationInfo(Class<?> sourceClass, AnnotatedElement annotatedElement, AnnotationFinder annotationFinder) {
		super();
		this.sourceClass = sourceClass;
		this.annotatedElement = annotatedElement;
		if(annotationFinder!=null){
			this.annotationFinder = annotationFinder;
		}else{
			this.annotationFinder = (annoElement, annoClass)->{
				return annoElement.getAnnotation(annoClass);
			};
		}
	}
	//使用spring相关类实现
	/*public AnnotationInfo(Class<?> sourceClass, Annotation...annoArray) {
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
	}*/


	public Class<?> getSourceClass() {
		return sourceClass;
	}

	public void setAnnotationFinder(AnnotationFinder annotationFinder) {
		this.annotationFinder = annotationFinder;
	}
	public List<Annotation> getAnnotations() {
		return ImmutableList.copyOf(this.annotatedElement.getAnnotations());
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
//		return (T)this.annotationMap.get(annoClass);
//		return annotatedElement.getAnnotation(annoClass);
		return annoClass.cast(annotationFinder.getAnnotation(this.annotatedElement, annoClass));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((annotatedElement == null) ? 0 : annotatedElement.hashCode());
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
		if (annotatedElement == null) {
			if (other.annotatedElement != null)
				return false;
		} else if (!annotatedElement.equals(other.annotatedElement))
			return false;
		if (sourceClass == null) {
			if (other.sourceClass != null)
				return false;
		} else if (!sourceClass.equals(other.sourceClass))
			return false;
		return true;
	}

	
}
