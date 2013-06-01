package org.onetwo.plugins.fmtagext.ui;

import java.util.List;


abstract public class PageUI {
	
	protected String definedName;
	protected String title;
	
	public PageUI() {
		this("main-content");
	}
	public PageUI(String definedName) {
		super();
		this.definedName = definedName;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDefinedName() {
		return definedName;
	}
	abstract public PageUI addUI(UIComponent component, Object data);
	abstract public List<DataUIComponent> getDataComponents();
}
