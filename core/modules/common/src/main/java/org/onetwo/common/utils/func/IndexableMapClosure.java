package org.onetwo.common.utils.func;

public interface IndexableMapClosure<T, R> {

	public R execute(T obj, int index);
}
