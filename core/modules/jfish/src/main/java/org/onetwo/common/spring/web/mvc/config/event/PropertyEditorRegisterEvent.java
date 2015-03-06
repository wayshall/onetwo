package org.onetwo.common.spring.web.mvc.config.event;

import java.util.List;

import org.springframework.beans.PropertyEditorRegistrar;

public class PropertyEditorRegisterEvent {
	private final List<PropertyEditorRegistrar> propertyEditorRegistrars;

	public PropertyEditorRegisterEvent(
			List<PropertyEditorRegistrar> propertyEditorRegistrars) {
		super();
		this.propertyEditorRegistrars = propertyEditorRegistrars;
	}

	public PropertyEditorRegisterEvent registerArgumentResolver(PropertyEditorRegistrar e){
		this.propertyEditorRegistrars.add(e);
		return this;
	}
	
}
