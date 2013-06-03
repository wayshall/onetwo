package org.onetwo.common.web.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

public class FilterChain implements Filter {

	private List<Filter> filterChain = new ArrayList<Filter>();
	
	@PostConstruct
	public void init() {
		filterChain.add(new JerichoHtmlFilter());
		filterChain.add(new DelicateHtmlFilter());
	}
	
	@Override
	public Object doFilter(Object entity) {
		Object rs = entity;
		for(Filter f : filterChain) {
			rs = f.doFilter(rs);
		}
		return rs;
	}

	public void add(Filter f) {
		this.filterChain.add(f);
	}
}
