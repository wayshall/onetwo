package org.onetwo.common.utils.func;

@FunctionalInterface
public interface ReturnableClosure<ARG, RESULT> {

	public RESULT execute(ARG object) ;
}
