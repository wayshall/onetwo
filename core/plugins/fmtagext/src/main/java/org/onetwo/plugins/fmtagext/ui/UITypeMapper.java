package org.onetwo.plugins.fmtagext.ui;

import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.EntityFormUIBuilder;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.FormFIeldUIBuilder;
import org.onetwo.plugins.fmtagext.ui.form.FormCheckboxUI;
import org.onetwo.plugins.fmtagext.ui.form.FormDatepickerUI;
import org.onetwo.plugins.fmtagext.ui.form.FormFieldUI;
import org.onetwo.plugins.fmtagext.ui.form.FormHiddenUI;
import org.onetwo.plugins.fmtagext.ui.form.FormSelectUI;
import org.onetwo.plugins.fmtagext.ui.form.FormTextInputUI;
import org.onetwo.plugins.fmtagext.ui.form.FormTextareaUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI;
import org.onetwo.plugins.fmtagext.uitils.UIType;
import org.onetwo.plugins.fmtagext.uitils.UIUtils;

public class UITypeMapper {
	
	private static final UITypeMapper instance = new UITypeMapper();
	
	public static final UITypeMapper getInstance(){
		return instance;
	}
	
	private Map<UIType, FormFIeldUIBuilder> formUIMapper = LangUtils.newHashMap();
	

	public UITypeMapper(){
		this.initBuilder();
	}
	
	protected void setFormFieldProperties(FormFieldUI formField, JFieldViewObject viewObj){
		UIUtils.setFormFieldProperties(formField, viewObj);
	}
	
	protected final void initBuilder(){
//		mapFormUI(UIType.DEFAULT, new FormFIeldUIBuilder(){
//			public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj) {
//				FormFieldUI formField = null;
//				if(viewObj.getField().isIdentify()){
//					if(!formBuilder.isNewEntityForm())
//						formField = form.addHidden(viewObj.getFormName(), viewObj.getFormValue());
//					return ;
//				}else{
//					formField = new FormTextInputUI(form, viewObj.getFormName(), viewObj.getLabel(), viewObj.getFormValue());
//					form.addFormField(formField);
//				}
//				
//				setFormFieldProperties(formField, viewObj);
//			}
//		});

		mapFormUI(UIType.FORM_TEXTAREA, new FormFIeldUIBuilder(){
			public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj) {
				FormFieldUI formField = null;
				formField = new FormTextareaUI(form, viewObj.getFormName(), viewObj.getLabel(), viewObj.getFormValue());
				form.addFormField(formField);
				
				setFormFieldProperties(formField, viewObj);
			}
		});

		mapFormUI(UIType.FORM_TEXT_INPUT, new FormFIeldUIBuilder(){
			public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj) {
				FormFieldUI formField = new FormTextInputUI(form, viewObj.getFormName(), viewObj.getLabel(), viewObj.getFormValue());
				setFormFieldProperties(formField, viewObj);
				form.addFormField(formField);
			}
		});

		mapFormUI(UIType.FORM_DATEPICKER, new FormFIeldUIBuilder(){
			public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj) {
				FormFieldUI formField = new FormDatepickerUI(form, viewObj.getFormName(), viewObj.getLabel(), viewObj.getFormValue());
//				formField.setValueFormat(DateUtil.Date_Time);
				setFormFieldProperties(formField, viewObj);
				form.addFormField(formField);
			}
		});

		mapFormUI(UIType.FORM_CHECKBOX, new FormFIeldUIBuilder(){
			public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj) {
				FormFieldUI formField = new FormCheckboxUI(form, viewObj.getFormName(), viewObj.getLabel());
				setFormFieldProperties(formField, viewObj);
				form.addFormField(formField);
			}
		});

		mapFormUI(UIType.FORM_HIDDEN, new FormFIeldUIBuilder(){
			public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj) {
				FormFieldUI formField = new FormHiddenUI(form, viewObj.getFormName(), viewObj.getLabel());
				formField.setValue(viewObj.getFormValue());
				setFormFieldProperties(formField, viewObj);
				form.addFormField(formField);
			}
		});

		mapFormUI(UIType.FORM_SELECT, new FormFIeldUIBuilder(){
			public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj) {
				FormSelectUI formField = new FormSelectUI(form, viewObj.getFormName(), viewObj.getLabel(), viewObj.getFormValue());
				setFormFieldProperties(formField, viewObj);
				form.addFormField(formField);
			}
		});
	}
	
	public final void mapFormUI(UIType type, FormFIeldUIBuilder component){
		this.formUIMapper.put(type, component);
	}
	
	public FormFIeldUIBuilder getFormFIeldUIBuilder(UIType type){
		return this.formUIMapper.get(type);
	}

}
