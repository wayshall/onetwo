package org.onetwo.common.web.view.jsp.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.web.view.jsp.BaseHtmlTag;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.web.servlet.tags.NestedPathTag;

@SuppressWarnings("serial")
public class FormTag extends BaseHtmlTag<FormTagBean> {
	
	private String template = TagUtils.getTagPage("form/spring-form.jsp");

	private String action;
	private String method;
	private boolean uploadFile;
	
	private Object dataProvider;
	
	private boolean showOnly;
	
	@Override
	public FormTagBean createComponent() {
		return new FormTagBean();
	}

	protected String resolveModelAttribute() {
		return component.getName();
	}
	
	protected void forSpringFormTag(){
		// Expose the form object name for nested tags...
		String modelAttribute = resolveModelAttribute();
		this.pageContext.setAttribute(org.springframework.web.servlet.tags.form.FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, modelAttribute, PageContext.REQUEST_SCOPE);
//		this.pageContext.setAttribute(org.springframework.web.servlet.tags.form.FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, modelAttribute, PageContext.REQUEST_SCOPE);
		this.pageContext.setAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME, modelAttribute + PropertyAccessor.NESTED_PROPERTY_SEPARATOR, PageContext.REQUEST_SCOPE);
	}
	
	@Override
	public int doStartTag() throws JspException {
		int rs = super.doStartTag();
		
		this.forSpringFormTag();
		
		Object data = dataProvider;
		if(data==null){
			data = this.pageContext.getRequest().getAttribute(getName());
		}
		FormDataProvider provider = createDataProvider(data);
		
		this.component.setProvider(provider);
		
		return rs;
	}
	
	public static FormDataProvider createDataProvider(Object data){
		FormDataProvider provider = null;
		if(FormDataProvider.class.isInstance(data)){
			provider = (FormDataProvider) data;
		}else{
			provider = new SimpleFormDataProvider(data);
		}
		return provider;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			BodyContent bc = this.getBodyContent();
			if(bc!=null){
				this.component.setBodyContent(bc.getString());
			}
			this.pageContext.include(getTemplate());
		} catch (Exception e) {
			throw new JspException("render grid error : " + e.getMessage(), e);
		} finally{
			clearComponentFromRequest(TagUtils.getFormVarName());
		}
		return EVAL_PAGE;
	}
	
	
	protected void populateComponent() throws JspException{
		super.populateComponent();
		
		component.setAction(action);
		component.setMethod(method);
		component.setUploadFile(uploadFile);
		component.setShowOnly(showOnly);
		
		setComponentIntoRequest(TagUtils.getFormVarName(), component);
	}

	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = TagUtils.getViewPage(template);
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

	public void setDataProvider(Object dataProvider) {
		this.dataProvider = dataProvider;
	}

	public void setShowOnly(boolean showOnly) {
		this.showOnly = showOnly;
	}

}
