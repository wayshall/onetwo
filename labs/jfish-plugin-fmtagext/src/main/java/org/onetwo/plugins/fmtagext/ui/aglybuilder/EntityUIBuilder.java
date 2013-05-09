package org.onetwo.plugins.fmtagext.ui.aglybuilder;

import org.onetwo.plugins.fmtagext.ui.UIComponent;



public interface EntityUIBuilder<T extends UIComponent> {

	public EntityUIBuilder<T> buildForEntity(String...excludeFields);
	
	public T buildUIComponent();
	
	public T retriveUIComponent();
	
}
