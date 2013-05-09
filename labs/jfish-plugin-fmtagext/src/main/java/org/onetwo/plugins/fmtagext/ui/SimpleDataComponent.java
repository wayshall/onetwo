package org.onetwo.plugins.fmtagext.ui;

public class SimpleDataComponent extends AbstractDataComponent {

	private final Object data;
	
	public SimpleDataComponent(UIComponent component, Object data) {
		super(component);
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}

}
