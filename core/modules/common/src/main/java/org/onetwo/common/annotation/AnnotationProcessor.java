package org.onetwo.common.annotation;



public interface AnnotationProcessor {
	
	public void doWithAnnotation(AnnoContext context);
	
	public boolean canDoAnnotation(AnnoContext context);

}
