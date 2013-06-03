package org.onetwo.common.utils.annotation;



public interface AnnotationProcessor {
	
	public void doWithAnnotation(AnnoContext context);
	
	public boolean canDoAnnotation(AnnoContext context);

}
