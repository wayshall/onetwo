package org.onetwo.plugins.fmtagext.ui.form;

import org.onetwo.plugins.fmtagext.ui.FmUIComponent;



public class FormTextInputUI extends FormFieldUI {

	public FormTextInputUI(String name, String label) {
		this(null, name, label, FmUIComponent.asProperty(name));
	}
	public FormTextInputUI(FormUI parent, String name, String label, String value) {
		super(parent, "ui-form-text-input");
		this.label = label;
		this.name = name;
		this.value = value;
	}

}
