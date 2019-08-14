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
        if (c==BigDecimal.class) {
        	return (BigDecimal)value;
        } else if(c==BigInteger.class) {
        	return new BigDecimal((BigInteger)value);
        } else if (c.getSuperclass() == Integer.class) {
            return BigDecimal.valueOf((Integer)value);
        } else if (c.getSuperclass() == Long.class) {
            return BigDecimal.valueOf((Long)value);
        } else if (c.getSuperclass() == Number.class) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        } else if (c == Boolean.class) {
            return BigDecimal.valueOf(((Boolean) value).booleanValue() ? 1L : 0L);
        } else if (c == Character.class) {
            return BigDecimal.valueOf(((Character) value).charValue());
        } else {
        	return new BigDecimal(value.toString().trim());
        }

	}

}
