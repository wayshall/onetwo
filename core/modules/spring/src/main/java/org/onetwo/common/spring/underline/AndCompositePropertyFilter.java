package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;

/*****
 * 组合filter
 * 所有filter返回true，才返回true
 * @author way
 *
 */
public class AndCompositePropertyFilter extends CompositePropertyFilter<AndCompositePropertyFilter> {

	@Override
	public boolean isCopiable(PropertyDescriptor toProperty, Object fromValue) {
		boolean rs = true;
		for(PropertyFilter filter : filters){
			rs = filter.isCopiable(toProperty, fromValue) & rs;
//			System.out.println("---> "+toProperty.getName()+", rs:"+rs);
		}
		return rs;
	}

}
