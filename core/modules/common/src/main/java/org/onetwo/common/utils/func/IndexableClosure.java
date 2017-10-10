package org.onetwo.common.utils.func;

@FunctionalInterface
public interface IndexableClosure<T> {

	public void execute(T obj, int index);
}
