package org.onetwo.plugins.fmtagext.ui.valuer;

import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.UIComponent;

abstract public class AbstractComponentUIValuer<T> extends AbstractUIValuer<T> implements ExprUIValuer<T> {
	
	protected final UIComponent component;
	protected String value;

	public AbstractComponentUIValuer(UIComponent ui){
		this.component = ui;
		if(FmUIComponent.class.isInstance(ui)){
			this.value = ((FmUIComponent)ui).getValue();
		}
	}
	public AbstractComponentUIValuer(UIComponent ui, String value) {
		this.value = value;
		this.component = ui;
	}
	
	abstract public T getUIValue(Object viewValue);

	public UIComponent getComponent() {
		return component;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	
}
