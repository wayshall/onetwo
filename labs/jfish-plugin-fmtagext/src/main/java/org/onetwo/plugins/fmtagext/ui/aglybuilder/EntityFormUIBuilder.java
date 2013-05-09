package org.onetwo.plugins.fmtagext.ui.aglybuilder;

import java.util.Collections;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtag.JFieldShowable;
import org.onetwo.plugins.fmtagext.ui.JFieldViewObject;
import org.onetwo.plugins.fmtagext.ui.UI;
import org.onetwo.plugins.fmtagext.ui.UITypeMapper;
import org.onetwo.plugins.fmtagext.ui.ViewEntry;
import org.onetwo.plugins.fmtagext.ui.form.FormButtonUI;
import org.onetwo.plugins.fmtagext.ui.form.FormFieldUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI;

public class EntityFormUIBuilder extends BaseEntityUIBuilder<FormUI>{

	protected FormUI form;
	private boolean newEntityForm;
	private UITypeMapper uimapper = UITypeMapper.getInstance();

	public EntityFormUIBuilder(Class<?> entityClass, boolean newEntityForm) {
		this(entityClass, "", newEntityForm);
	}

	public EntityFormUIBuilder(Class<?> entityClass, String name, boolean newEntityForm) {
		super(entityClass);
		this.form = new FormUI(name, "");
		this.newEntityForm = newEntityForm;
	}
	

	public FormUI buildUIComponent() {
		Collections.sort(form.getFields());
		return form;
	}

	@Override
	public EntityFormUIBuilder buildForEntity(String...excludeFields) {
		this.beforeBuildEntityFields();
		this.buildEntityFields(excludeFields);
		this.afterBuildEntityFields();
		return this;
	}
	
	private void buildEntityFields(String...excludeFields) {
		ViewEntry entry = this.getJFishViewEntry(entityClass);
		
		if(StringUtils.isBlank(form.getName())){
			String name = StringUtils.uncapitalize(entityClass.getSimpleName());
			name = StringUtils.trimEndWith(name, "Model");
			form.setName(name);
		}

		this._buildTitlte(form, entry);
		
		for(JFieldViewObject field : entry.getFields()){
			if(!isAcceptedField(excludeFields, field.getName()))
				continue;

			if((newEntityForm && !field.canShowIn(JFieldShowable.create))
					||
				(!newEntityForm && !field.canShowIn(JFieldShowable.update))
			){
				continue ;
			}
			
//			this.everyFieldForBuild(viewObj);
//			this.getFieldUIBuilder(field.getName()).buildFormField(this, form, viewObj);
			this.buildFormField(form, field);
		}
		
	}
	public void buildFormField(FormUI form, JFieldViewObject viewObj) {
		uimapper.getFormFIeldUIBuilder(viewObj.getFormui()).buildFormField(this, form, viewObj);
	}
//	public void buildFormField(FormUI form, JFieldViewObject viewObj) {
//		FormFieldUI formField = null;
//		if(viewObj.getField().isIdentify()){
//			if(!isNewEntityForm())
//				formField = form.addHidden(viewObj.getName(), viewObj.getFormValue());
//		}else{
//			formField = new FormTextInputUI(form, viewObj.getFormName(), viewObj.getLabel(), viewObj.getFormValue());
//			form.addFormField(formField);
//		}
//		if(formField!=null){
//			formField.setShowOrder(viewObj.getOrder());
//		}
//	}
	
	public EntityFormUIBuilder buildButtons(FormButtonUI...buttons){
		form.getButtons().clear();
		form.addButtons(buttons);
		return this;
	}

	public EntityFormUIBuilder buildFormField(FormFieldUI field){
		this.form.addFormField(field);
		return this;
	}
	
	public FormUI retriveUIComponent() {
		return form;
	}

	public EntityFormUIBuilder buildFormAction(String method, String action) {
		this.form.setFormAction(method, action);
		return this;
	}

	public boolean isNewEntityForm() {
		return newEntityForm;
	}

	public void buildEntityFormCommonButtons(String listPath){
		buildButtons(UI.btnOk(), listPath==null?null:UI.backToButton(listPath));
	}
}
