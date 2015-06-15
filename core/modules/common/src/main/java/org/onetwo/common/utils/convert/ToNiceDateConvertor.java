package org.onetwo.common.utils.convert;

import java.util.Date;

import org.onetwo.common.utils.NiceDate;

public class ToNiceDateConvertor extends AbstractTypeConvert<NiceDate> {
	
	protected ToNiceDateConvertor() {
		super(NiceDate.New(new Date(0)));
	}

	@Override
	public NiceDate doConvert(Object value, Class<?> componentType) {
//		if(value==null)
//			return null;
		Class<?> vtype = value.getClass();
		NiceDate date = null;
		if(Date.class.isAssignableFrom(vtype)){
			date = NiceDate.New((Date)value);
		}else{
			throw new IllegalArgumentException("only support convert Date to NiceDate");
		}
		return date;
	}

}
