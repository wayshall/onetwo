package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;

/*****
 * 组合filter
 * 任何一个filter返回true，即返回true
 * @author way
 *
 */
public class OrCompositePropertyFilter extends CompositePropertyFilter<OrCompositePropertyFilter> {

	@Override
	public boolean isCopiable(PropertyDescriptor toProperty, Object fromValue) {
		for(PropertyFilter filter : filters){
			if(filter.isCopiable(toProperty, fromValue))
				return true;
		}
		return false;
	}

}
