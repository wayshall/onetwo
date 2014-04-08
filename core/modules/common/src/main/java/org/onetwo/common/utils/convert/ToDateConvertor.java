package org.onetwo.common.utils.convert;

import java.util.Date;

import org.onetwo.common.utils.DateUtil;

public class ToDateConvertor extends AbstractTypeConvert<Date> {
	
	@Override
	public Date doConvert(Object value, Class<?> componentType) {
//		if(value==null)
//			return null;
		Class<?> vtype = value.getClass();
		Date date = null;
		if(Date.class.isAssignableFrom(vtype)){
			date = new Date(((Date)value).getTime());
		}else if(CharSequence.class.isAssignableFrom(vtype)){
			date = DateUtil.parse(value.toString());
		}else if(Number.class.isAssignableFrom(vtype)){
			date = new Date(((Number)value).longValue());
		}
		return date;
	}

}
