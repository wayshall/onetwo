package org.onetwo.common.spring.validator;

import java.lang.annotation.ElementType;

import jakarta.validation.Path;
import jakarta.validation.TraversableResolver;

public class EmptyTraversableResolver implements TraversableResolver {

	public final boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
		return true;
	}

	public final boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
		return true;
	}
}
