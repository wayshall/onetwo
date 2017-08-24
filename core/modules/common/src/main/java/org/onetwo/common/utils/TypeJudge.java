package org.onetwo.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface TypeJudge {

	public Object ifIterable(Iterable<?> obj);
	public Object ifList(List<?> obj);
	public Object ifCollection(Collection<?> obj);
	public Object ifMap(Map<?, ?> obj);
	public Object ifArray(Object[] array);
//	public Object ifPrimitiveArray(Object array, Class<?> primitiveType);
	public Object other(Object obj, Class<?> toType);
	public Object all(Object obj);
}
