package org.onetwo.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class TypeJudgeAdapter implements TypeJudge {

	@Override
	public Object ifList(List<?> obj) {
		return obj;
	}
	
	public Object ifCollection(Collection<?> obj) {
		return obj;
	}

	public Object ifMap(Map<?, ?> obj) {
		return obj;
	}

	@Override
	public Object ifArray(Object[] array) {
		return array;
	}
	
	/*@Override
	public Object ifPrimitiveArray(Object array, Class<?> primitiveType) {
		return array;
	}*/

	public Object other(Object obj, Class<?> toType) {
		Object result;
		if (toType == Long.class || toType == long.class) {
			result = ifLong(obj);
		} else if (toType == Character.class || toType == char.class) {
			result = ifCharacter(obj);
		} else if (toType == Byte.class || toType == byte.class) {
			result = ifByte(obj);
		} else if (toType == Short.class || toType == short.class) {
			result = ifInteger(obj);
		} else if (toType == Integer.class || toType == int.class) {
			result = ifInteger(obj);
		} else if (toType == Double.class || toType == double.class) {
			result = ifDouble(obj);
		} else if (toType == Float.class || toType == float.class) {
			result = ifFloat(obj);
		} else if (toType == Boolean.class || toType == boolean.class) {
			result = ifBoolean(obj);
		} else if (toType == String.class) {
			result = ifString(obj);
		} else {
			result = ifNotBaseType(obj);
		}
		return result;
	}
	
	public Object all(Object obj) {
		return obj;
	}

	public Object ifBoolean(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifByte(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifCharacter(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifDouble(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifFloat(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifInteger(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifShort(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifLong(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifString(Object obj) {
		return defaultValueIfOhter(obj);
	}

	public Object ifNotBaseType(Object obj) {
		return defaultValueIfOhter(obj);
	}
	
	protected Object defaultValueIfOhter(Object obj){
		return obj;
	}
}
