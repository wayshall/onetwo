package org.onetwo.common.spring.aop;

import org.onetwo.common.spring.aop.Mixin.MixinFrom;

/**
 * @author wayshall
 * <br/>
 */
public class MixinAttrs {
	
	final private Class<?> implementor;
	final private MixinFrom from;
	
	public MixinAttrs(Class<?> implementor, MixinFrom from) {
		super();
		this.implementor = implementor;
		this.from = from;
	}
	public Class<?> getImplementor() {
		return implementor;
	}
	public MixinFrom getFrom() {
		return from;
	}

}
