package org.onetwo.common.convert;

import java.util.Date;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.utils.StringUtils;

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
	    	String str = value.toString().trim();
	    	if (StringUtils.isBlank(str)) {
	    		return null;
	    	}
			date = DateUtils.parse(str);
		}else if(Number.class.isAssignableFrom(vtype)){
			date = new Date(((Number)value).longValue());
		}
		return date;
	}

}
