package org.onetwo.plugins.fmtagext.ui;

import java.util.List;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

public class SimplePageUI extends PageUI {
	
	private List<DataUIComponent> dataComponents = LangUtils.newArrayList();
	
	public SimplePageUI() {
		super();
	}
	public SimplePageUI(String definedName) {
		super(definedName);
	}

	public SimplePageUI addUI(UIComponent component, Object data){
		Assert.notNull(component);
		dataComponents.add(new SimpleDataComponent(component, data));
		return this;
	}

	public List<DataUIComponent> getDataComponents() {
		return dataComponents;
	}
}
