package org.onetwo.common.db.event;

import java.lang.annotation.Annotation;

import org.onetwo.common.utils.annotation.AnnoContext;
import org.onetwo.common.utils.annotation.AnnotationProcessor;
import org.onetwo.common.utils.annotation.AnnotationProcessorManager;
import org.onetwo.common.utils.annotation.DefaultAnnotationProcessorManager;

public class DefaultEventListener {
	
	private AnnotationProcessorManager annotationProcessorManager;

	public DefaultEventListener(){
		this.annotationProcessorManager = new DefaultAnnotationProcessorManager();
		this.init();
	}
	
	protected DefaultEventListener(AnnotationProcessorManager annotationProcessorManager) {
		super();
		this.annotationProcessorManager = annotationProcessorManager;
		this.init();
	}
	
	protected void init(){
	}

	public void autoProcess(AnnoContext context) {
		annotationProcessorManager.autoProcess(context);
	}

	public AnnotationProcessorManager registerProcessor(Class<? extends Annotation> annoClass, AnnotationProcessor processor) {
		return annotationProcessorManager.registerProcessor(annoClass, processor);
	}
	
}
