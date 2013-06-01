package org.onetwo.common.ioc.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.InjectProcessor;
import org.onetwo.common.ioc.InnerContainer;
import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.inject.AnnotationFacotry;
import org.onetwo.common.ioc.inject.InjectAnnotationParser;
import org.onetwo.common.ioc.inject.InjectMeta;
import org.onetwo.common.ioc.inject.impl.FieldInjectMeta;
import org.onetwo.common.ioc.inject.impl.PropertyInjectMeta;
import org.onetwo.common.utils.ReflectUtils;

public class DefaultInjectProcessor implements InjectProcessor {
	
	private InnerContainer innerContainer;
	private List<InjectAnnotationParser> annotaionParsers = AnnotationFacotry.getInstance().getInjectAnnotaionParsers();
	
	DefaultInjectProcessor(){
		this.annotaionParsers = AnnotationFacotry.getInstance().getInjectAnnotaionParsers();
	}
	
	public DefaultInjectProcessor(InnerContainer container){
		this();
		this.innerContainer = container;
	}
	
	public void setInnerContainer(InnerContainer innerContainer) {
		this.innerContainer = innerContainer;
	}

	public InjectProcessor addAnnotationParser(InjectAnnotationParser parser){
		annotaionParsers.add(parser);
		return this;
	}
	
	public void inject(Object object){
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(object.getClass());
		for(Field field : fields){
			injectByField(object, field);
		}
		PropertyDescriptor[] propDescriptors = ReflectUtils.desribProperties(object.getClass());
		for(PropertyDescriptor propDes : propDescriptors){
			this.injectByProperty(object, propDes);
		}
	}
	
	protected void injectByProperty(Object object, PropertyDescriptor propertyDescriptor){
		if(propertyDescriptor.getWriteMethod()==null)
			return ;
		InjectMeta meta = new PropertyInjectMeta(object.getClass(), propertyDescriptor);
		injectByAnnotationParser(object, meta);
	}
	
	protected void injectByField(Object object, Field field){
		InjectMeta meta = new FieldInjectMeta(field);
		injectByAnnotationParser(object, meta);
	}
	
	protected void injectByAnnotationParser(Object object, InjectMeta meta){
		ObjectInfo objInfo = null;
		for(InjectAnnotationParser parser : this.annotaionParsers){
			if(!parser.containsAnnotation(meta))
				continue;
			try {
				objInfo = parser.parse(meta, innerContainer);
				meta.inject(object, objInfo);
			} catch (Exception e) {
				String msg = "inject error : [object="+object.getClass()+", fieldName="+meta.getName()+"]";
				throw new ServiceException(msg, e);
			}
			/*if(value!=null){
				meta.inject(object, value);
				break;
			}else
				throw new ServiceException("at least one match object["+object.getClass()+"] for the field [name="+meta.getName()+", type="+meta.getInjectType()+"]");*/
		}
	}
}
