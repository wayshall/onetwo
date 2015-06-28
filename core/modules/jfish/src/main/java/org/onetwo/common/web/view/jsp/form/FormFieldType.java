package org.onetwo.common.web.view.jsp.form;

/****
 * add:
 * FormUIFactory
 * FormFieldTag#populateComponent
 * field.tag
 * @author weishao
 *
 */
public enum FormFieldType {
	input,
	text,
	date,
	password,
	file,
	textarea,
	select,
	radio,
	radioGroup,
	checkbox,
	checkboxGroup,
	hidden,
	button,
	submit,
	html,
	unknow;
	
	public static FormFieldType of(String type){
		for(FormFieldType ftype: values()){
			if(ftype.name().equalsIgnoreCase(type)){
				return ftype;
			}
		}
		return unknow;
	}
}
