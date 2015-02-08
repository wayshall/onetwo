package org.onetwo.common.spring.web.mvc.config.event;

import java.util.List;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;

public class ArgumentResolverEvent {

	final private List<HandlerMethodArgumentResolver> argumentResolvers;

	public ArgumentResolverEvent(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		super();
		this.argumentResolvers = argumentResolvers;
	}
	
	public ArgumentResolverEvent registerArgumentResolver(HandlerMethodArgumentResolver e){
		this.argumentResolvers.add(e);
		return this;
	}
	
}
