package org.onetwo.plugins.fmtagext.ui.form;

import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.valuer.StringUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;

public class FormFieldUI extends FmUIComponent implements Comparable<FormFieldUI>{

	protected boolean hiddenField;
	
	protected int showOrder;
	
	public FormFieldUI(FormUI parent, String template) {
		super(parent, template);
	}
	
	public FormUI getForm(){
		return (FormUI)getParent();
	}

	public String getLabel() {
		return label;
	}

	public final void setLabel(String label) {
		this.label = label;
	}

	public UIValuer<?> getUivaluer() {
		if(uivaluer==null){
			this.uivaluer = new StringUIValuer(this, getValue(), valueFormat);
		}
		return uivaluer;
	}

	public boolean isHiddenField() {
		return hiddenField;
	}

	public void setHiddenField(boolean hiddenField) {
		this.hiddenField = hiddenField;
	}

	@Override
	public int compareTo(FormFieldUI o) {
		return showOrder-o.showOrder;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

}
