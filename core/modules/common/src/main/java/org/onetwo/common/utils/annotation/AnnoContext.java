package org.onetwo.common.utils.annotation;

import java.lang.annotation.Annotation;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AnnoContext {
	
	public static AnnoContext create(Object srcObject, Enum eventAction){
		return new AnnoContext(srcObject, eventAction);
	}

	private Object srcObject;
	private Enum eventAction;
	private Object annoIn;
	private Annotation annotation;
	
	protected AnnoContext(){
	}
	
	public AnnoContext(Object srcObject, Enum eventAction) {
		super();
		this.srcObject = srcObject;
		this.eventAction = eventAction;
	}
	
	public <T> T getSrcObject() {
		return (T)srcObject;
	}
	public <T> T getAnnoIn() {
		return (T)annoIn;
	}
	public Annotation getAnnotation() {
		return annotation;
	}

	public void setSrcObject(Object srcObject) {
		this.srcObject = srcObject;
	}

	public void setAnnoIn(Object annoIn) {
		this.annoIn = annoIn;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public Enum getEventAction() {
		return eventAction;
	}

	public void setEventAction(Enum eventAction) {
		this.eventAction = eventAction;
	}
	
}
