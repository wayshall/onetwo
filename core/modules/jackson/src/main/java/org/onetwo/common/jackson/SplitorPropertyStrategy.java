package org.onetwo.common.jackson;

import org.onetwo.common.utils.StringUtils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

/***
 * toJson的时候，把驼峰命名的属性转换为下划线
 * 
 * @author way
 *
 */
public class SplitorPropertyStrategy extends PropertyNamingStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String splitor = "_";
	
	public SplitorPropertyStrategy() {
		super();
	}

	public SplitorPropertyStrategy(String splitor) {
		super();
		this.splitor = splitor;
	}

	@Override
	public String nameForField(MapperConfig<?> config, AnnotatedField field,
			String defaultName) {
		return convertName(defaultName);
	}

	@Override
	public String nameForGetterMethod(MapperConfig<?> config,
			AnnotatedMethod method, String defaultName) {
		return convertName(defaultName);
	}

	@Override
	public String nameForSetterMethod(MapperConfig<?> config,
			AnnotatedMethod method, String defaultName) {
		return convertName(defaultName);
	} 

	/****
	 * 大小写转换为下划线
	 * @author weishao zeng
	 * @param name
	 * @return
	 */
	private String convertName(String name){
		return StringUtils.convertWithSeperator(name, splitor);
	}


}
