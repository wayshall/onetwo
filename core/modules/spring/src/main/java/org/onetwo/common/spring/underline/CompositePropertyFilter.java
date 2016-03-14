package org.onetwo.common.spring.underline;

import java.util.List;

import com.google.common.collect.Lists;

/*****
 * 组合filter
 * 所有filter返回true，才返回true
 * @author way
 *
 */
abstract public class CompositePropertyFilter<F extends CompositePropertyFilter<F>> implements PropertyFilter {

	protected final List<PropertyFilter> filters = Lists.newArrayList();
	
	public F add(PropertyFilter filter){
		filters.add(filter);
		return (F)this;
	}

	public void clear() {
		filters.clear();
	}

	public boolean isEmpty() {
		return filters.isEmpty();
	}

}
