package org.onetwo.plugins.fmtagext.ui;

abstract public class AbstractDataComponent implements DataUIComponent, RendableUI {

	private final UIComponent component;
	
	public AbstractDataComponent(UIComponent component) {
		super();
		this.component = component;
	}

	@Override
	public UIComponent getComponent() {
		return component;
	}


	@Override
	public void onRender(UIRender render) {
		render.setObjectVariable(getVarNameOnTemplate(), this);
		render.renderUIComponent(getComponent());
	}
	
	protected String getVarNameOnTemplate(){
		return "__"+DataUIComponent.class.getSimpleName()+"__";
	}
	
}
