package org.onetwo.plugins.fmtagext.ui;

import java.util.List;

public interface ContainerUIComponent {
	
	public List<UIComponent> getChildren();
	
	public void addChild(UIComponent uiComponent);
	
	public boolean isEmpty();

}
