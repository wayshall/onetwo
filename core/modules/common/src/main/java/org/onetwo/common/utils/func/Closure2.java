package org.onetwo.common.utils.func;

@FunctionalInterface
public interface Closure2<T1, T2> {

	public void execute(T1 obj1, T2 obj2);
}
