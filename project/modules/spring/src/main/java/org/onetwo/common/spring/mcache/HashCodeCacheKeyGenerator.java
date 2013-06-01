package org.onetwo.common.spring.mcache;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

/*****
 * copy from spring modules
 *
 */
public class HashCodeCacheKeyGenerator implements CacheKeyGenerator {
	 private boolean generateArgumentHashCode;

	  /**
	   * Construct a <code>HashCodeCacheKeyGenerator</code>.
	   */
	  public HashCodeCacheKeyGenerator() {
	    super();
	  }

	  /**
	   * Construct a <code>HashCodeCacheKeyGenerator</code>.
	   * 
	   * @param generateArgumentHashCode
	   *          the new value for the flag that indicates if this generator should
	   *          generate the hash code of the arguments passed to the method to
	   *          apply caching to. If <code>false</code>, this generator uses
	   *          the default hash code of the arguments.
	   */
	  public HashCodeCacheKeyGenerator(boolean generateArgumentHashCode) {
	    this();
	    setGenerateArgumentHashCode(generateArgumentHashCode);
	  }
	  

	  public final Serializable generateKey(MethodInvocation methodInvocation) {
		  return generateKey(methodInvocation.getMethod(), methodInvocation.getArguments());
	  }

	  /**
	   * @see CacheKeyGenerator#generateKey(MethodInvocation)
	   */
	  public final Serializable generateKey(Method method, Object... methodArguments) {
	    HashCodeCalculator hashCodeCalculator = new HashCodeCalculator();

	    if(method!=null)
	    	hashCodeCalculator.append(System.identityHashCode(method));

	    if (methodArguments != null) {
	      int methodArgumentCount = methodArguments.length;

	      for (int i = 0; i < methodArgumentCount; i++) {
	        Object methodArgument = methodArguments[i];
	        int hash = 0;

	        if (generateArgumentHashCode) {
	          hash = Objects.reflectionHashCode(methodArgument);
	        } else {
	          hash = Objects.nullSafeHashCode(methodArgument);
	        }

	        hashCodeCalculator.append(hash);
	      }
	    }

	    long checkSum = hashCodeCalculator.getCheckSum();
	    int hashCode = hashCodeCalculator.getHashCode();

	    Serializable cacheKey = new HashCodeCacheKey(checkSum, hashCode);
	    return cacheKey;
	  }

	  /**
	   * Sets the flag that indicates if this generator should generate the hash
	   * code of the arguments passed to the method to apply caching to. If
	   * <code>false</code>, this generator uses the default hash code of the
	   * arguments.
	   * 
	   * @param newGenerateArgumentHashCode
	   *          the new value of the flag
	   */
	  public final void setGenerateArgumentHashCode(
	      boolean newGenerateArgumentHashCode) {
	    generateArgumentHashCode = newGenerateArgumentHashCode;
	  }
}
