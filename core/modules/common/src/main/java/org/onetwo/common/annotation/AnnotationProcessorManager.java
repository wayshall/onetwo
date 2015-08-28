package org.onetwo.common.annotation;

import java.lang.annotation.Annotation;

public interface AnnotationProcessorManager {

//	public AnnotationProcessor<Annotation> getProcessor(Class<? extends Annotation> annoClass);
	public AnnotationProcessorManager registerProcessor(Class<? extends Annotation> annoClass, AnnotationProcessor processor);
//	public void doInProperties(Object obj, Class<? extends Annotation>... annoClass);
	public void autoProcess(AnnoContext context); 

}
