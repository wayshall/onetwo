package org.onetwo.common.utils.list;

import org.onetwo.common.exception.BaseException;



abstract public class NoIndexIt<T> implements It<T>{

	@Override
	public boolean doIt(T element, int index) {
		try {
			doIt(element);
		} catch (Exception e) {
			throw new BaseException("iterator occur error: "+e.getMessage(), e);
		}
		return true;
	}
	
	abstract protected void doIt(T element) throws Exception ; 
}
