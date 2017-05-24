package org.onetwo.common.spring.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
public class FreezableImpl extends DelegatingIntroductionInterceptor implements Freezable {
    private boolean freeze;

    @Override
	public void freeze() {
    	this.freeze = true;
	}

	@Override
	public void unfreeze() {
		this.freeze = false;
	}

	@Override
	public boolean freezed() {
		return freeze;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
        if (freezed() && invocation.getMethod().getName().indexOf("set") == 0)
        	throw new IllegalStateException("locked");
        return super.invoke(invocation);
   }

}
