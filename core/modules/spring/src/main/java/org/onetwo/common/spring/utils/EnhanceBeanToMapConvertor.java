package org.onetwo.common.spring.utils;

import java.beans.PropertyDescriptor;
import java.util.Optional;

import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.BeanWrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wayshall
 * <br/>
 */
public class EnhanceBeanToMapConvertor extends BeanToMapConvertor {
	protected static class SpringObjectWrapper implements ObjectWrapper {
		final private BeanWrapper bw;

		public SpringObjectWrapper(Object object) {
			super();
			this.bw = SpringUtils.newBeanWrapper(object);
		}
		public PropertyDescriptor[] desribProperties() {
			return bw.getPropertyDescriptors();
		}
		public Object getPropertyValue(PropertyDescriptor prop) {
			return bw.getPropertyValue(prop.getName());
		}
		
		public boolean isReadableProperty(PropertyDescriptor prop) {
			return bw.isReadableProperty(prop.getName());
		}
		public boolean isWritableProperty(PropertyDescriptor prop) {
			return bw.isWritableProperty(prop.getName());
		}
	}
	

	@Override
	protected ObjectWrapper objectWrapper(Object obj) {
		return new DefaultObjectWrapper(obj);
	}
	
	public static class JsonPropertyConvert extends DefaultPropertyNameConvertor {
		private boolean enableJsonPropertyAnnotation = false;
		
		public JsonPropertyConvert(boolean enableJsonPropertyAnnotation, boolean enableFieldNameAnnotation, boolean enableUnderLineStyle) {
			super(enableFieldNameAnnotation, enableUnderLineStyle);
			this.enableJsonPropertyAnnotation = enableJsonPropertyAnnotation;
		}

		@Override
		public String convert(ObjectPropertyContext ctx) {
			String name = super.convert(ctx);
			if(enableJsonPropertyAnnotation){
				Optional<JsonProperty> jp = AnnotationUtils.findAnnotationOnPropertyOrField(ctx.getSource().getClass(), ctx.getProperty(), JsonProperty.class);
				if(jp.isPresent()){
					name = jp.get().value();
				}
			}
			return name;
		}
		
	}


	public static class EnhanceBeanToMapBuilder extends BaseBeanToMapBuilder<EnhanceBeanToMapBuilder> {
		public static EnhanceBeanToMapBuilder createFrom(BaseBeanToMapBuilder<?> builder){
			EnhanceBeanToMapBuilder enhance = new EnhanceBeanToMapBuilder();
			builder.copyTo(enhance);
			return enhance;
		}
		public static EnhanceBeanToMapBuilder enhanceBuilder(){
			return new EnhanceBeanToMapBuilder();
		}
		private boolean enableJsonPropertyAnnotation = false;

		public EnhanceBeanToMapBuilder enableJsonPropertyAnnotation() {
			this.enableJsonPropertyAnnotation = true;
			return this;
		}

		public EnhanceBeanToMapBuilder ignoreNull() {
			this.propertyAcceptor = new IgnoreNullValuePropertyAcceptor(this.propertyAcceptor);
			return self();
		}

		public EnhanceBeanToMapConvertor build(){
			EnhanceBeanToMapConvertor beanToFlatMap = new EnhanceBeanToMapConvertor();
			beanToFlatMap.setPrefix(prefix);
			beanToFlatMap.setPropertyAcceptor(propertyAcceptor);
			beanToFlatMap.setValueConvertor(valueConvertor);
			if(flatableObject!=null){
				beanToFlatMap.setFlatableObject(flatableObject);
			}
			

			if (this.propertyNameConvertor==null) {
				beanToFlatMap.setPropertyNameConvertor(new JsonPropertyConvert(enableJsonPropertyAnnotation, 
																				enableFieldNameAnnotation, 
																				enableUnderLineStyle));
			} else {
				beanToFlatMap.setPropertyNameConvertor(this.propertyNameConvertor);
			}
			
			return beanToFlatMap;
		}
	}

}
