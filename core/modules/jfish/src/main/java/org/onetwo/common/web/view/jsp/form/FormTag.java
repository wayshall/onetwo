package org.onetwo.common.web.view.jsp.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.preventor.PreventorFactory;
import org.onetwo.common.web.preventor.RequestPreventor;
import org.onetwo.common.web.view.jsp.BaseHtmlTag;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.web.servlet.tags.NestedPathTag;

@SuppressWarnings("serial")
public class FormTag extends BaseHtmlTag<FormTagBean> {
//	public final static String CODE_PREFIX = "code:";
	
	private String template = "form/spring-form.jsp";
	private RequestPreventor csrfPreventor = PreventorFactory.getCsrfPreventor();

	private String action;
	private String method;
	private boolean uploadFile;
	
	private Object dataProvider;
	
	private boolean showOnly;
	private boolean disabled;
//	private String editPermission;
	
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
		Object model = this.pageContext.getRequest().getAttribute(getName());
		component.setModel(model);
		
		this.forSpringFormTag();
		
		Object data = dataProvider;
		if(data==null){
			data = model;
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
		BodyContent bc = this.getBodyContent();
		if(bc!=null){
			this.component.setBodyContent(bc.getString());
		}
		try {
			String t = getThemeSetting().getTagPage(getTemplate());
			renderTemplate(t);
		} finally{
			clearComponentFromRequest(TagUtils.getFormVarName());
		}
		return EVAL_PAGE;
	}
	
	
	protected void populateComponent() throws JspException{
		super.populateComponent();
		

		component.setAction(buildActionString());
		component.setMethod(method);
		component.setUploadFile(uploadFile);
		component.setShowOnly(showOnly);
		
		component.setDisabled(disabled);
		
		setComponentIntoRequest(TagUtils.getFormVarName(), component);
	}
	
	protected String buildActionString() {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		if(StringUtils.isBlank(action)){
			String surl = TagUtils.getRequestUri(request);
			return surl;
		}
//		this.buildQueryString = false;
		if(action.startsWith(":")){
			return TagUtils.parseAction(request, action, this.csrfPreventor);
		}
		return action;
	}
	
	public String getTemplate() {
		return this.template;
	}
	public void setTemplate(String template) {
		this.template = template;
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


	public void setEditPermission(String editPermission) {
		this.disabled = !checkPermission(editPermission);
	}

	public boolean isDisabled() {
		return disabled;
	}

	
}
