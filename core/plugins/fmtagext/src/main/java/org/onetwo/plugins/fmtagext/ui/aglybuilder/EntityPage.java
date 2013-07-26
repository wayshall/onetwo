package org.onetwo.plugins.fmtagext.ui.aglybuilder;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.spring.rest.RestPather.EntityPathInfo;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.plugins.fmtagext.ui.JFieldViewObject;
import org.onetwo.plugins.fmtagext.ui.PageUI;
import org.onetwo.plugins.fmtagext.ui.UI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI;

public class EntityPage<T>{

	private Map<String, FieldUIBuilder> overrideFieldBuilders = LangUtils.newHashMap();
	private Collection<FieldUIBuilder> addFieldBuilders;
	
	protected EntityFormUIBuilder newFormBuilder;
	protected EntityFormUIBuilder editFormBuilder;
	protected EntityGridUIBuilder listgridBuilder;
	protected EntityGridUIBuilder dateListgridBuilder;
	protected ShowEntityGridUIBuilder showgridBuilder;
	
	protected Class<T> entityClass;
	protected EntityPathInfo entityPathInfo;

	public EntityPage(EntityPathInfo entityPathInfo){
		this(null, entityPathInfo);
	}
	
	public EntityPage(Class<T> entityClass, EntityPathInfo entityPathInfo){
		if(entityClass==null)
			this.entityClass = ReflectUtils.getSuperClassGenricType(this.getClass());
		else
			this.entityClass = entityClass;
		this.entityPathInfo = entityPathInfo;
	}
	
	public final void overrideField(String name, FieldUIBuilder fieldBuilder){
		overrideFieldBuilders.put(name, fieldBuilder);
	}
	
	public final void addField(FieldUIBuilder fieldBuilder){
		if(addFieldBuilders==null){
			addFieldBuilders = LangUtils.newArrayList();
		}
		addFieldBuilders.add(fieldBuilder);
	}
	
	public FieldUIBuilder getFieldUIBuilder(String name){
		FieldUIBuilder builder = overrideFieldBuilders.get(name);
		return builder;
	}
	
	protected EntityFormUIBuilder newEntityFormUIBuilder(boolean newEntityForm){
		return new PageEntityFormUIBuilder(entityClass, newEntityForm);
	}

	public EntityFormUIBuilder initNewFormUIBuilder(String...excludeFields){
		return initNewFormUIBuilder(entityPathInfo, ":backUrl", excludeFields);
	}
	public EntityFormUIBuilder initNewFormUIBuilder(EntityPathInfo formAction, String backListAction, String...excludeFields){
		Assert.notNull(formAction);
		EntityFormUIBuilder newFormBuilder = newEntityFormUIBuilder(true);
		newFormBuilder.buildFormAction(formAction.getCreatePathInfo().getMethod(), formAction.getCreatePathInfo().getPath());
		newFormBuilder.buildForEntity(excludeFields);
		newFormBuilder.buildEntityFormCommonButtons(backListAction);
		return newFormBuilder;
	}

	public EntityFormUIBuilder initEditFormUIBuilder(String...excludeFields){
		return initEditFormUIBuilder(entityPathInfo, ":backUrl", excludeFields);
	}

	public EntityFormUIBuilder initEditFormUIBuilder(EntityPathInfo formAction, String backListAction, String...excludeFields){
		EntityFormUIBuilder editFormBuilder = newEntityFormUIBuilder(false);
		editFormBuilder.buildFormAction(formAction.getUpdatePathInfo().getMethod(), formAction.getUpdatePathInfo().getPath());
		editFormBuilder.buildForEntity(excludeFields);
		editFormBuilder.buildEntityFormCommonButtons(backListAction);
		return editFormBuilder;
	}
	
	protected EntityGridUIBuilder newEntityGridUIBuilder(){
		return new PageEntityGridUIBuilder(entityClass);
	}
	public EntityGridUIBuilder initListgridBuilder(String...excludeFields){
		EntityGridUIBuilder listgridBuilder = newEntityGridUIBuilder();
		listgridBuilder.buildCheckbox().buildForm(entityPathInfo.getBatchDeletePathInfo().getMethod(), entityPathInfo.getBatchDeletePathInfo().getPath());
		listgridBuilder.buildForEntity(excludeFields);
		listgridBuilder.buildGridOperationsWithCommons(entityPathInfo);
		listgridBuilder.buildGridToobarMenusWithCommons(entityPathInfo);
		return listgridBuilder;
	}

	
	protected ShowEntityGridUIBuilder newShowEntityGridUIBuilder(){
		return new PageShowEntityGridUIBuilder(entityClass, "");
	}
	public ShowEntityGridUIBuilder initShowgridBuilder(String...excludeFields){
		ShowEntityGridUIBuilder showgridBuilder = newShowEntityGridUIBuilder();
		showgridBuilder.buildForEntity(excludeFields);
		showgridBuilder.buildShowGridToobarMenusWithCommons(entityPathInfo);
		return showgridBuilder;
	}
	
	public void build(){
		newFormBuilder = this.initNewFormUIBuilder();
		newFormBuilder.buildUIComponent();
		editFormBuilder = this.initEditFormUIBuilder();
		editFormBuilder.buildUIComponent();
		
		listgridBuilder = this.initListgridBuilder();
		listgridBuilder.buildUIComponent();
		showgridBuilder = this.initShowgridBuilder();
		showgridBuilder.buildUIComponent();
	}

	public PageUI getListPage(Page<?> data){
		return UI.page(this.listgridBuilder.retriveUIComponent(), data);
	}
	
	public PageUI getShowPage(T entity){
		return UI.page(this.showgridBuilder.retriveUIComponent(), entity);
	}
	
	public PageUI getNewPage(T entity){
		return UI.page(this.newFormBuilder.retriveUIComponent(), entity);
	}
	
	public PageUI getEditPage(T entity){
		return UI.page(this.editFormBuilder.retriveUIComponent(), entity);
	}


	
	protected class PageEntityFormUIBuilder extends EntityFormUIBuilder {

		public PageEntityFormUIBuilder(Class<?> entityClass, boolean newEntityForm) {
			super(entityClass, newEntityForm);
		}

		public void buildFormField(FormUI form, JFieldViewObject viewObj) {
			FieldUIBuilder builder = getFieldUIBuilder(viewObj.getName());
			if(builder!=null)
				builder.buildFormField(this, form, viewObj);
			else
				super.buildFormField(form, viewObj);
		}

		protected void afterBuildEntityFields() {
			if(LangUtils.isEmpty(addFieldBuilders))
				return ;
			for(FieldUIBuilder fb : addFieldBuilders){
				fb.buildFormField(this, form, null);
			}
		}
		
	}
	
	protected class PageShowEntityGridUIBuilder extends ShowEntityGridUIBuilder {

		public PageShowEntityGridUIBuilder(Class<?> entityClass, String title) {
			super(entityClass, title);
		}

		
		public void buildShowGridField(DataGridUI datagrid, JFieldViewObject viewObj) {
			FieldUIBuilder builder = getFieldUIBuilder(viewObj.getName());
			if(builder!=null)
				builder.buildShowGridField(this, datagrid, viewObj);
			else
				super.buildShowGridField(datagrid, viewObj);
		}
		
		protected void afterBuildEntityFields() {
			if(LangUtils.isEmpty(addFieldBuilders))
				return ;
			for(FieldUIBuilder fb : addFieldBuilders){
				fb.buildShowGridField(this, datagrid, null);
			}
		}
	}

	protected class PageEntityGridUIBuilder extends EntityGridUIBuilder {

		public PageEntityGridUIBuilder(Class<?> entityClass) {
			super(entityClass);
		}

		
		public void buildListGridField(DataGridUI datagrid, JFieldViewObject viewObj) {
			FieldUIBuilder builder = getFieldUIBuilder(viewObj.getName());
			if(builder!=null)
				builder.buildListGridField(this, datagrid, viewObj);
			else
				super.buildListGridField(datagrid, viewObj);
		}
		
		protected void afterBuildEntityFields() {
			if(LangUtils.isEmpty(addFieldBuilders))
				return ;
			for(FieldUIBuilder fb : addFieldBuilders){
				fb.buildListGridField(this, datagrid, null);
			}
		}
	}
}
