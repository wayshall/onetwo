package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;
import java.util.List;

import com.google.common.collect.Lists;

/*****
 * 组合filter
 * 所有filter返回true，才返回true
 * @author way
 *
 */
public class CompositePropertyFilter implements PropertyFilter {

	private final List<PropertyFilter> filters = Lists.newArrayList();
	
	public CompositePropertyFilter add(PropertyFilter filter){
		filters.add(filter);
		return this;
	}
	
	@Override
	public boolean isCopiable(PropertyDescriptor toProperty, Object fromValue) {
		boolean rs = true;
		for(PropertyFilter filter : filters){
			rs = filter.isCopiable(toProperty, fromValue) & rs;
		}
		return rs;
	}

}
