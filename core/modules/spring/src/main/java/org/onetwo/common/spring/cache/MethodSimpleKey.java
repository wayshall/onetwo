package org.onetwo.common.spring.cache;

import java.util.Arrays;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author wayshall
 * <br/>
 */
public class MethodSimpleKey {

	public static final MethodSimpleKey EMPTY = new MethodSimpleKey();

	private Object[] params;
	private int hashCode;

	public void setParams(Object... elements) {
		Assert.notNull(elements, "Elements must not be null");
		this.params = new Object[elements.length];
		System.arraycopy(elements, 0, this.params, 0, elements.length);
		this.hashCode = Arrays.deepHashCode(this.params);
	}
	

	public Object[] getParams() {
		return params;
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj || (obj instanceof MethodSimpleKey
				&& Arrays.deepEquals(this.params, ((MethodSimpleKey) obj).params)));
	}

	@Override
	public final int hashCode() {
		return this.hashCode;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
	}

}
