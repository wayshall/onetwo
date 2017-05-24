package org.onetwo.common.spring.aop;
/**
 * @author wayshall
 * <br/>
 */
@Mixin(FreezableImpl.class)
public interface Freezable {

	void freeze();
    void unfreeze();
    boolean freezed();
}
