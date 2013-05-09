package org.onetwo.plugins.fmtagext.ui.form;

import org.onetwo.plugins.fmtagext.ui.FmUIComponent;



public class FormDatepickerUI extends FormTextInputUI {

	public FormDatepickerUI(String name, String label) {
		this(null, name, label, FmUIComponent.asProperty(name));
	}

	public FormDatepickerUI(String name, String label, String format) {
		this(null, name, label, FmUIComponent.asProperty(name));
		this.setValueFormat(format);
	}
	public FormDatepickerUI(FormUI parent, String name, String label, String value) {
		super(parent, name, label, value);
		this.setTemplate("ui-form-datepicker");
	}

}
