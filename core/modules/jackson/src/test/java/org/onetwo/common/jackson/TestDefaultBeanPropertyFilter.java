package org.onetwo.common.jackson;

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class TestDefaultBeanPropertyFilter extends SimpleBeanPropertyFilter {

	@Override
	protected boolean include(BeanPropertyWriter writer) {
		return !"email".equals(writer.getName());
	}

	

}
