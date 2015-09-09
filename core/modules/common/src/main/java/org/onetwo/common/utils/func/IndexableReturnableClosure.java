package org.onetwo.common.utils.func;

public interface IndexableReturnableClosure<T, R> {

	public R execute(T obj, int index);
}
