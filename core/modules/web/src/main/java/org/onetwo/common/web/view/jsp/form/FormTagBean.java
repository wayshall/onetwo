package org.onetwo.common.web.view.jsp.form;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.view.HtmlElement;

public class FormTagBean extends HtmlElement {

	private String action;
	private String method;
	private boolean uploadFile;

	private String bodyContent;

	private List<FormFieldTagBean> fields = LangUtils.newArrayList();
	private List<FormFieldTagBean> buttons = LangUtils.newArrayList(3);
	private FormFieldTagBean submit;

	private FormDataProvider provider;
	private boolean showOnly;

	public void addField(FormFieldTagBean field) {
		field.setFormBean(this);
		if(field.getType()==FormFieldType.submit){
			this.submit = field;
		}else if(field.getType()==FormFieldType.button){
			this.buttons.add(field);
		}else{
			this.fields.add(field);
		}
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(boolean uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public List<FormFieldTagBean> getFields() {
		return fields;
	}

	public FormDataProvider getProvider() {
		return provider;
	}

	public void setProvider(FormDataProvider provider) {
		this.provider = provider;
	}

	public FormFieldTagBean getSubmit() {
		return submit;
	}

	public List<FormFieldTagBean> getButtons() {
		return buttons;
	}
	
	public String getEncType(){
		return uploadFile?"multipart/form-data":"application/x-www-form-urlencoded";
	}
	
	public String getId(){
		return super.getId()+"Form";
	}

	public boolean isShowOnly() {
		return showOnly;
	}

	public void setShowOnly(boolean showOnly) {
		this.showOnly = showOnly;
	}

}
