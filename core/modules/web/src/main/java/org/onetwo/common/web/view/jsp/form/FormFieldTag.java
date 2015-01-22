package org.onetwo.common.web.view.jsp.form;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;
import org.onetwo.common.web.view.jsp.BaseHtmlTag;
import org.onetwo.common.web.view.jsp.TagUtils;

import com.google.common.collect.ImmutableMap;

public class FormFieldTag extends BaseHtmlTag<FormFieldTagBean>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5324408407472195764L;
	private FormFieldType formFieldType = FormFieldType.input;
	private String type;
	private String provider;
	private boolean showErrorTag = true;
	private String errorPath;
//	private String render;
	private String dataFormat;
	private String value;
	
	//select, checkobx, radio
	private Object items;
	private String itemLabel = "name";
	private String itemValue = "value";
	private String emptyOptionLabel;

	private boolean readOnly;
	private boolean disabled;
	

//	private String permission;
//	private boolean showable = true;
	private boolean modelAttribute = true;
	
	private boolean showLoadingText = true;

//	private boolean ignoreField;
	private final Map<String, ExtFormFieldPopulater> pupulaterMap;
	
	public FormFieldTag(){
		Map<String, ExtFormFieldPopulater> temp = LangUtils.newHashMap();
		List<ExtFormFieldPopulater> populaters = SpringApplication.getInstance().getBeans(ExtFormFieldPopulater.class);
		for(ExtFormFieldPopulater fp : populaters){
			temp.put(fp.getProvider(), fp);
		}
		pupulaterMap = ImmutableMap.copyOf(temp);
	}
	
	
	@Override
	public FormFieldTagBean createComponent() {
		if(StringUtils.isNotBlank(provider)){
			ExtFormFieldPopulater populater = getPopulater();
			if(populater!=null){
				return populater.createTagBean(this);
			}
		}
		return FormUIFactory.createUIBean(formFieldType);
	}
	
	protected ExtFormFieldPopulater getPopulater(){
		return this.pupulaterMap.get(provider);
	}
	
	protected FormTagBean getFormTagBean(){
		return getComponentFromRequest(TagUtils.getFormVarName(), FormTagBean.class);
	}

	protected void populateComponent() throws JspException{
		super.populateComponent();
		
		component.setType(formFieldType);
		component.setErrorTag(showErrorTag);
		component.setErrorPath(errorPath);
//		component.setRender(render);
		component.setValue(value);
		component.setDataFormat(dataFormat);
		component.setReadOnly(readOnly);
		component.setDisabled(disabled);
		component.setModelAttribute(modelAttribute);
		component.setShowLoadingText(showLoadingText);
		
		switch (formFieldType) {
			case input:
			case password:
				break;
			case hidden:
				component.setErrorTag(false);
				break;
			case textarea:
			case radio:
			case file:
			case checkbox:
				break;
			case select:
			case radioGroup:
			case checkboxGroup:
				populateSelect((FormItemsTagBean)component);
				break;
			case button:
			case submit:
				component.setErrorTag(false);
				break;
	
			default:
				/*ExtFormFieldPopulater populater = this.pupulaterMap.get(typeString);
				if(populater!=null){
					populater.populateFieldComponent(this, component);
				}*/
				break;
		}
		
		if(StringUtils.isNotBlank(provider)){
			ExtFormFieldPopulater populater = getPopulater();
			if(populater!=null){
				populater.populateFieldComponent(this, component);
			}
		}
		
//		this.component.buildTagAttributesString();
	}
	
	private void populateSelect(FormItemsTagBean sl){
		sl.setItemLabel(itemLabel);
		sl.setItemValue(itemValue);
		Object itemDatas = null;
		if(String.class.isInstance(items)){
			itemDatas = this.pageContext.getRequest().getAttribute(items.toString());	
		}else{
			itemDatas = this.items;
		}
		sl.setItemDatas(itemDatas);
		sl.setEmptyOptionLabel(emptyOptionLabel);
	}
	
	/*@Override
	public int doStartTag() throws JspException {
		this.ignoreField = this.checkIgnoreField();
		if(ignoreField)
			return SKIP_BODY;
		
		return super.doStartTag();
	}*/
	
	@Override
	public int endTag() throws JspException {
//		if(ignoreField)
//			return EVAL_PAGE;
		
		FormTagBean formBean = getFormTagBean();
		if(formBean==null)
			throw new JspException("no parent form tag found for field : " + getName());
		
		if(component.isHtmlTypeRender()){
			BodyContent bc = getBodyContent();
			if(bc!=null)
				component.setBodyContent(bc.getString());
		}
		if("valid".equals(component.getName())){
			System.out.println("test");
		}
		formBean.addField(component);
		return EVAL_PAGE;
	}


	public void setType(String type) {
		this.type = type;
		if(StringUtils.isBlank(type)){
			this.formFieldType = FormFieldType.input;
		}else{
			this.formFieldType = FormFieldType.of(type);
			if(this.formFieldType==FormFieldType.unknow){
				throw new IllegalArgumentException("unknow type: " + type);
			}
		}
	}

	/****
	 * 只要不是false和no字符串，即为true
	 * @param errorTag
	 */
	public void setErrorTag(String errorTag) {
		this.showErrorTag = Types.convertValue(errorTag, boolean.class);
		this.errorPath = errorTag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setItems(Object items) {
		this.items = items;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void setDisabled(String disabled) {
		String codePrefix = "code:";
		if(disabled.startsWith(codePrefix)){
			String code = disabled.substring(codePrefix.length());
			this.disabled = !checkPermission(code);
		}else{
			this.disabled = Types.convertValue(disabled, boolean.class);
		}
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setShowable(boolean showable) {
		this.showable = showable;
	}

	public void setModelAttribute(boolean modelAttribute) {
		this.modelAttribute = modelAttribute;
		if(this.modelAttribute==false)
			this.setErrorTag("false");
	}

	public void setEmptyOptionLabel(String emptyOptionLabel) {
		this.emptyOptionLabel = emptyOptionLabel;
	}

	public void setShowLoadingText(boolean showLoadingText) {
		this.showLoadingText = showLoadingText;
	}


	public String getItemLabel() {
		return itemLabel;
	}


	public String getItemValue() {
		return itemValue;
	}


	public String getEmptyOptionLabel() {
		return emptyOptionLabel;
	}


	public String getErrorPath() {
		return errorPath;
	}


	public Object getItems() {
		return items;
	}

	public boolean isReadOnly() {
		return readOnly;
	}


	public boolean isModelAttribute() {
		return modelAttribute;
	}


	public String getProvider() {
		return provider;
	}


	public void setProvider(String provider) {
		this.provider = provider;
	}


	public FormFieldType getFormFieldType() {
		return formFieldType;
	}

	public String getType() {
		return type;
	}



}
