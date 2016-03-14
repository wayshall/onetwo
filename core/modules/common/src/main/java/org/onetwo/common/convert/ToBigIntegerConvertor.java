package org.onetwo.common.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ToBigIntegerConvertor extends AbstractTypeConvert<BigInteger> {

	protected ToBigIntegerConvertor() {
		super(BigInteger.valueOf(0));
	}

	@Override
	public BigInteger doConvert(Object value, Class<?> componentType) {
//		if (value == null)
//            return BigInteger.valueOf(0L);
        Class<?> c = value.getClass();
        if(c==BigInteger.class)
        	return (BigInteger)value;
        if(c==BigDecimal.class)
        	return ((BigDecimal)value).toBigInteger();
        if (c.getSuperclass() == Number.class)
            return BigInteger.valueOf(((Number) value).longValue());
        if (c == Boolean.class)
            return BigInteger.valueOf(((Boolean) value).booleanValue() ? 1L : 0L);
        if (c == Character.class)
            return BigInteger.valueOf(((Character) value).charValue());
        return new BigInteger(value.toString().trim());

	}

}
