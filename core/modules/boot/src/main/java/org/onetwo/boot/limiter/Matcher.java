package org.onetwo.boot.limiter;


/**
 * @author wayshall
 * <br/>
 */
public interface Matcher<CTX> {
	
	boolean matches(CTX context);
	
}
