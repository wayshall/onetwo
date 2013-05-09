package org.onetwo.plugins.fmtagext.ui.form;




public class FormHiddenUI extends FormFieldUI {


	public FormHiddenUI(String name) {
		this(null, name, asProperty(name));
	}

	public FormHiddenUI(String name, String value) {
		this(null, name, value);
	}
	public FormHiddenUI(FormUI parent, String name, String value) {
		super(parent, "ui-form-hidden");
		this.name = name;
		this.value = value;
		this.hiddenField = true;
	}

}
