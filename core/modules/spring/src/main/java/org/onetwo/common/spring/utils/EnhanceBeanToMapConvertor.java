package org.onetwo.common.spring.utils;

import java.beans.PropertyDescriptor;
import java.util.Optional;

import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.FieldName;
import org.onetwo.common.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wayshall
 * <br/>
 */
public class EnhanceBeanToMapConvertor extends BeanToMapConvertor {
	private boolean enableJsonPropertyAnnotation = false;
	
	
	
	@Override
	protected PropertyContext createPropertyContext(Object obj, PropertyDescriptor prop) {
		return new PropertyContext(obj, prop, prop.getName()){

			public String getName() {
				String name = this.name;
				if(source!=null){
					if(enableFieldNameAnnotation){
						FieldName fn = ReflectUtils.getFieldNameAnnotation(source.getClass(), name);
						if(fn!=null){
							name = fn.value();
							//直接返回,和父类实现稍有不同
							return name;
						}
					}
					if(enableJsonPropertyAnnotation){
						Optional<JsonProperty> jp = AnnotationUtils.findAnnotationOnPropertyOrField(source.getClass(), getProperty(), JsonProperty.class);
						if(jp.isPresent()){
							name = jp.get().value();
							return name;
						}
					}
				}
				if(enableUnderLineStyle){
					name = StringUtils.convert2UnderLineName(name);
				}
				return name;
			}
		};
	}



	public static class EnhanceBeanToMapBuilder extends BaseBeanToMapBuilder<EnhanceBeanToMapBuilder> {
		public static EnhanceBeanToMapBuilder enhanceBuilder(){
			return new EnhanceBeanToMapBuilder();
		}
		private boolean enableJsonPropertyAnnotation = false;

		public EnhanceBeanToMapBuilder enableJsonPropertyAnnotation() {
			this.enableJsonPropertyAnnotation = true;
			return this;
		}

		public EnhanceBeanToMapConvertor build(){
			EnhanceBeanToMapConvertor beanToFlatMap = new EnhanceBeanToMapConvertor();
			beanToFlatMap.setPrefix(prefix);
			beanToFlatMap.setPropertyAcceptor(propertyAcceptor);
			beanToFlatMap.setValueConvertor(valueConvertor);
			beanToFlatMap.setFlatableObject(flatableObject==null?DEFAULT_FLATABLE:flatableObject);
			beanToFlatMap.enableFieldNameAnnotation = enableFieldNameAnnotation;
			beanToFlatMap.enableUnderLineStyle = enableUnderLineStyle;
			beanToFlatMap.enableJsonPropertyAnnotation = enableJsonPropertyAnnotation;
			return beanToFlatMap;
		}
	}

}
