package org.onetwo.plugins.fmtagext.ui.form;



public class FormCheckboxUI extends FormFieldUI {

	private boolean checkAll;
	
	public FormCheckboxUI(boolean checkall, String name, String label) {
		this(null, name, label);
		this.checkAll = checkall;
	}
	public FormCheckboxUI(FormUI parent, String name, String label) {
		super(parent, "ui-form-checkbox");
		this.label = label;
		this.name = name;
	}
	
	public boolean isCheckAll() {
		return checkAll;
	}
	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}
	public String getCssClass(){
		if(checkAll){
			return "dg-checkbox-all";
		}else{
			return "dg-checkbox-field";
		}
	}

}
