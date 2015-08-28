package org.onetwo.common.annotation;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;

public class DefaultAnnotationProcessorManager implements AnnotationProcessorManager {
	
	private Map<Class<? extends Annotation>, AnnotationProcessor> processors = new HashMap<Class<? extends Annotation>, AnnotationProcessor>();
	
	public DefaultAnnotationProcessorManager(){
	}

//	@Override
	public AnnotationProcessor getProcessor(Class<? extends Annotation> annoClass) {
		AnnotationProcessor processor = processors.get(annoClass);
		if(processor==null)
			LangUtils.throwBaseException("can not find the prossor: " + annoClass);
		return processor;
	}

	@Override
	public AnnotationProcessorManager registerProcessor(Class<? extends Annotation> annoClass, AnnotationProcessor processor) {
		processors.put(annoClass, processor);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public void autoProcess(AnnoContext context){
		doInProperties(context, processors.keySet().toArray(new Class[processors.size()]));
	}
	
	public void doInProperties(AnnoContext context, Class<? extends Annotation>... annoClasses){
		Object srcObject = context.getSrcObject();
		PropertyDescriptor[] props = ReflectUtils.desribProperties(srcObject.getClass());
		if(props==null || props.length==0)
			return ;
		Annotation annotation = null;
		for(PropertyDescriptor p : props){
			for(Class<? extends Annotation> annoClass : annoClasses){
				annotation = this.getAnnotation(srcObject, p, annoClass);
				if(annotation!=null){
					AnnotationProcessor processor = getProcessor(annoClass);
					appendToContext(context, p, annotation);
					if(processor.canDoAnnotation(context)){
						getProcessor(annoClass).doWithAnnotation(context);
					}
				}
			}
		}
	}
	
	protected Annotation getAnnotation(Object srcObject, Object annoIn, Class<? extends Annotation> annoClass){
		Annotation annotation = null;
		PropertyDescriptor p = (PropertyDescriptor) annoIn;
		annotation = p.getReadMethod().getAnnotation(annoClass);
		/*String name = "itineraryCode";
		if(p.getName().equals(name)){
			try {
				System.out.println("p: " + p);
				Method g = ReflectUtils.findMethod(srcObject.getClass(), "get"+StringUtils.toJavaName(name, true));
				annotation = AnnotationUtils.findAnnotation(g, annoClass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		if(annotation==null && Serializable.class.equals(p.getPropertyType())){
			String getMethod = ReflectUtils.getReadMethodName(p.getName(), p.getPropertyType());
			Method g = ReflectUtils.findMethod(srcObject.getClass(), getMethod);
			annotation = AnnotationUtils.findAnnotation(g, annoClass);
		}
		return annotation;
	}
	
	protected void appendToContext(AnnoContext context, Object annoIn, Annotation annotation){
		context.setAnnoIn(annoIn);
		context.setAnnotation(annotation);
	}


}
