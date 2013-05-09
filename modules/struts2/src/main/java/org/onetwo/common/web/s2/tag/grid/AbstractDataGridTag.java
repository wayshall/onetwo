package org.onetwo.common.web.s2.tag.grid;

import org.onetwo.common.web.s2.tag.WebUIClosingTag;

abstract public class AbstractDataGridTag extends WebUIClosingTag {

	protected int colspan = 1;

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
	

	public AbstractDataGridComponent getComponent(){
		return (AbstractDataGridComponent) this.component;
	}
	
	protected void populateParams() {
		super.populateParams();
		AbstractDataGridComponent comp = getComponent();
		comp.setColspan(colspan);
	}
	
}
