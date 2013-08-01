package org.onetwo.plugins.fmtagext.ui.datagrid;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtagext.ui.ContainerUIComponent;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.UI;
import org.onetwo.plugins.fmtagext.ui.valuer.StringUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;

public class DataGridColumnUI extends FmUIComponent{

	private int colspan = 1;
	
	private ContainerUIComponent container;
	
	public DataGridColumnUI(){
		this("");
	}
	
	public DataGridColumnUI(String value) {
		this(value, value);
	}
	
	public DataGridColumnUI(String name, String value) {
		super("ui-grid-column");
		this.value = value;
		this.name = name;
	}
	
	public DataGridColumnUI(ContainerUIComponent uiContainer) {
		super(null, "ui-grid-column");
		this.container = uiContainer;
	}
	
	public DataGridColumnUI(FmUIComponent... innerUI) {
		super("ui-grid-column");
		if(!LangUtils.isEmpty(innerUI)){
			this.container = UI.container();
			for(FmUIComponent inner : innerUI){
				inner.setParent(this);
				this.container.addChild(inner);
			}
		}
//		this.value = innerUI.getValue();
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public String getComponentTemplate(){
		return super.getComponentTemplate();
	}

	public ContainerUIComponent getContainer() {
		return container;
	}
	
	public final DataGridColumnUI addUIComponent(FmUIComponent uiComponent){
		if(container==null){
			container = UI.container();
		}
		uiComponent.setParent(this);
		container.addChild(uiComponent);
		return this;
	}

	public void setUiContainer(ContainerUIComponent uiContainer) {
		this.container = uiContainer;
	}
	
	public boolean hasContainer(){
		return container!=null && !container.isEmpty();
	}

	@Override
	protected UIValuer<?> createUIValuer(String value, String valueFormat) {
		return new StringUIValuer(this, value, valueFormat){
			public String getUIValue(Object viewValue) {
				if(value==null &&viewValue!=null)
					return viewValue.toString();
				return super.getUIValue(viewValue);
			}
		};
	}
}
