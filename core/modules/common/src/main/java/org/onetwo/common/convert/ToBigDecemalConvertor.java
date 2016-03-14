package org.onetwo.common.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ToBigDecemalConvertor extends AbstractTypeConvert<BigDecimal> {

	protected ToBigDecemalConvertor() {
		super(BigDecimal.valueOf(0));
	}

	@Override
	public BigDecimal doConvert(Object value, Class<?> componentType) {
//		if (value == null)
//            return BigDecimal.valueOf(0L);
        Class<?> c = value.getClass();
        if(c==BigDecimal.class)
        	return (BigDecimal)value;
        if(c==BigInteger.class)
        	return new BigDecimal((BigInteger)value);
        if (c.getSuperclass() == Number.class)
            return BigDecimal.valueOf(((Number) value).longValue());
        if (c == Boolean.class)
            return BigDecimal.valueOf(((Boolean) value).booleanValue() ? 1L : 0L);
        if (c == Character.class)
            return BigDecimal.valueOf(((Character) value).charValue());
        return new BigDecimal(value.toString().trim());

	}

}
