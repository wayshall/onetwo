package org.onetwo.plugins.fmtagext.ui;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.rest.RestPather.EntityPathInfo;
import org.onetwo.common.spring.rest.RestPather.PathInfo;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.ui.form.FormButtonUI;
import org.onetwo.plugins.fmtagext.ui.form.FormCheckboxUI;
import org.onetwo.plugins.fmtagext.ui.form.FormLinkButtonUI;
import org.onetwo.plugins.fmtagext.ui.form.FormSubmitUI;
import org.onetwo.plugins.fmtagext.ui.form.FormTextInputUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI.FormMethod;
import org.onetwo.plugins.fmtagext.ui.form.LinkUI;

final public class UI {
	
	public static class TemplateKeys {
		public static final String UI_GRID = "ui-grid";
		public static final String UI_GRID_CHECKBOX = "ui-grid-checkbox";
		public static final String UI_SHOW_GRID = "ui-show-grid";
		public static final String UI_GRID_ROW = "ui-grid-row";
		public static final String UI_GRID_HEADER_ROW = "ui-grid-header-row";
		public static final String UI_GRID_ITERATOR_ROW = "ui-grid-iterator-row";
		public static final String UI_MENU_BAR = "ui-menu-bar";
		

		public static final String UI_FORM = "ui-form";
	}
	
	public static class CssClassKeys {
		public static final String DG_TOOLBAR_BUTTON_BATCH = "dg-toolbar-button-batch";
	}
	
	public static FormButtonUI btnOk(){
		return button("btn_ok", " 提 交 ", null, null);
	}
	
	public static FormSubmitUI submit(){
		FormSubmitUI submit = new FormSubmitUI("", " 提 交 ");
		return submit;
	}
	/*public static FormButtonUI btnBack(String href){
		FormButtonUI btn = button("btn_back", " 返 回 ", null, href);
		btn.setLocationHref(true);
		btn.setDataConfirm("false");
		return btn;
	}*/
	public static FormButtonUI locationButton(String name, String label, String href){
		FormButtonUI btn = button(name, label, null, href);
		btn.setLocationHref(true);
		btn.setDataConfirm("false");
		return btn;
	}
	
	public static FormButtonUI editEntityButton(EntityPathInfo entityPathInfo){
		return UI.locationButton("btnEdit", "编辑", entityPathInfo.getEditPathInfo().getPath());
	}
	
	public static FormButtonUI backToButton(String path){
		return UI.locationButton("btnBack", "返回", path);
	}
	
	public static FormLinkButtonUI linkButton(String label, PathInfo pathInfo){
		FormLinkButtonUI btn = new FormLinkButtonUI("", label);
		btn.setDataConfirm("false");
		btn.setMethodAction(pathInfo.getMethod(), pathInfo.getPath());
		return btn;
	}
	public static FormButtonUI button(String name, String label, FormMethod method, String action){
		FormButtonUI btn = new FormButtonUI(name, label);
		if(method!=null)
			btn.setDataMethod(method);
		if(StringUtils.isNotBlank(action))
			btn.setDataAction(action);
		return btn;
	}

	public static FormCheckboxUI gridCheckboxAll(){
		FormCheckboxUI cb = new FormCheckboxUI(true, "id_all", "全选");
		cb.setCheckAll(true);
		cb.setTemplate(TemplateKeys.UI_GRID_CHECKBOX);
		return cb;
	}

	public static FormCheckboxUI gridCheckbox(String name, String label, String value){
		FormCheckboxUI cb = new FormCheckboxUI(false, name, label);
		if(StringUtils.isNotBlank(value))
			cb.setValue(value);
		cb.setTemplate(TemplateKeys.UI_GRID_CHECKBOX);
		return cb;
	}
	
	public static LinkUI link(String label, String href){
		LinkUI link = new LinkUI(null, label, href);
//		link.setUivaluer(new StringUIValuer(link, href));
		return link;
	}
	
	public static LinkUI editLink(EntityPathInfo entityPathInfo){
		return UI.link("编辑  ", entityPathInfo.getEditPathInfo().getPath());
	}
	
	public static LinkUI showLink(EntityPathInfo entityPathInfo){
		return UI.link("查看  ", entityPathInfo.getShowPathInfo().getPath());
	}
	
	public static LinkUI newLink(EntityPathInfo entityPathInfo, String...paramNames){
		return UI.link("新建", entityPathInfo.getNewPathInfo().getPath()).addParamNames(paramNames);
	}
	
	public static FormLinkButtonUI batchDeleteLink(EntityPathInfo entityPathInfo){
		return UI.linkButton("批量删除", entityPathInfo.getBatchDeletePathInfo());
	}
	
	public static ContainerUIComponent container(){
		return container("");
	}
	
	public static ContainerUIComponent container(String tag){
		return new DefaultContainerUIComponent(tag);
	}

	public static PageUI page(UIComponent component, Object data){
		SimplePageUI page = new SimplePageUI();
		page.addUI(component, data);
		return page;
	}
	
	public static FormTextInputUI input(String name, String label){
		FormTextInputUI input = new FormTextInputUI(name, label);
		return input;
	}
	
	public static <T> T jsonNew(Class<T> uiClass, String json){
		Assert.notNull(uiClass);
		T ui = JsonMapper.IGNORE_NULL.fromJson(json, uiClass);
		return ui;
	}
	
	public static <T> T jsonIt(T ui, String json){
		Assert.notNull(ui);
		T updateUI = JsonMapper.IGNORE_NULL.update(json, ui);
		return updateUI;
	}
	
	private UI(){}
}
