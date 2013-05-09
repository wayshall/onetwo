package org.onetwo.common.utils.list;

public interface PredicateBlock<T> {
	
	abstract public boolean evaluate(T element, int index);

}
