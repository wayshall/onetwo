package org.onetwo.common.utils.list;


abstract public class NoIndexIt<T> implements It<T>{

	@Override
	public boolean doIt(T element, int index) throws Exception {
		doIt(element);
		return true;
	}
	
	abstract protected void doIt(T element) throws Exception ; 
}
