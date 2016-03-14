package org.onetwo.common.convert;

import java.util.Date;

import org.onetwo.common.date.DateUtil;

public class ToDateConvertor extends AbstractTypeConvert<Date> {
	
	protected ToDateConvertor() {
		super(new Date(0));
	}

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
