package org.onetwo.dbm.ui.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.mapping.DbmEnumValueMapping;
import org.onetwo.dbm.ui.exception.DbmUIException;
import org.onetwo.dbm.ui.vo.EnumDataVO;
import org.springframework.beans.BeanWrapper;

/**
 * @author weishao zeng
 * <br/>
 */

final public class Duis {
	
	public static List<EnumDataVO> toEnumDataVO(Class<? extends Enum<?>> enumClass) {
		Enum<?>[] values = (Enum<?>[]) enumClass.getEnumConstants();
		List<EnumDataVO> list = Stream.of(values).map(ev -> {
			EnumDataVO data = toEnumDataVO(ev);
			return data;
		}).collect(Collectors.toList());
		return list;
	}
	
	public static EnumDataVO toEnumDataVO(Object beanData) {
		EnumDataVO enumData = new EnumDataVO();
		BeanWrapper bw = SpringUtils.newBeanWrapper(beanData);
		String label = (String)bw.getPropertyValue("label");
		
		if (beanData instanceof DbmEnumValueMapping) {
			DbmEnumValueMapping<?> e = (DbmEnumValueMapping<?>) beanData;
			enumData.setValue(e.getEnumMappingValue());
		} else if (bw.isReadableProperty("value")) {
			Object value = bw.getPropertyValue("value");
			enumData.setValue(value);
		} else if (beanData instanceof Enum<?>){
			enumData.setValue(((Enum<?>)beanData).name());
		} else {
			throw new DbmUIException("property[value] not found for select data: " + beanData);
		}
		enumData.setLabel(label);
		return enumData;
	}
	
	private Duis() {
	}

}
