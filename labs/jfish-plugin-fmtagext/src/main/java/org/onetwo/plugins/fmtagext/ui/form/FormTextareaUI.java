package org.onetwo.plugins.fmtagext.ui.form;

import org.onetwo.plugins.fmtagext.ui.FmUIComponent;



public class FormTextareaUI extends FormFieldUI {

	public FormTextareaUI(String name, String label) {
		this(null, name, label, FmUIComponent.asProperty(name));
	}
	public FormTextareaUI(FormUI parent, String name, String label, String value) {
		super(parent, "ui-form-textarea");
		this.label = label;
		this.name = name;
		this.value = value;
	}

}
