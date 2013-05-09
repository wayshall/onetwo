package org.onetwo.common.utils.annotation;

import org.onetwo.common.utils.ArrayUtils;


@SuppressWarnings("rawtypes")
abstract public class AbstractAnnotationProcessor<T extends Enum> implements AnnotationProcessor{
	
	private T[] listenActions;
	
	public AbstractAnnotationProcessor(T... action) {
		super();
		this.listenActions = action;
	}

	public T[] getListenActions() {
		return listenActions;
	}
	
	public boolean canDoAnnotation(AnnoContext context){
		return context.getAnnotation()!=null && ArrayUtils.contains(listenActions, context.getEventAction());
	}

	@Override
	public void doWithAnnotation(AnnoContext context) {
		this.doAnnotation(context);
	}
	
	abstract public void doAnnotation(AnnoContext context);


}
