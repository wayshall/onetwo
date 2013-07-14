package org.onetwo.plugins.codegen.page;

import org.onetwo.common.spring.rest.RestPather.EntityPathInfo;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.onetwo.plugins.fmtagext.ui.PageUI;
import org.onetwo.plugins.fmtagext.ui.UI;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.EntityGridUIBuilder;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.EntityPage;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridColumnUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridUI;
import org.onetwo.plugins.fmtagext.ui.form.FormCheckboxUI;
import org.onetwo.plugins.fmtagext.ui.form.FormLinkButtonUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI.FormMethod;
import org.onetwo.plugins.fmtagext.ui.valuer.SimpleComponentUIValuer;

public class DatabasePage extends EntityPage<DatabaseEntity>{

	protected EntityGridUIBuilder showTablesGridBuilder;
	
	public DatabasePage(EntityPathInfo entityPathInfo) {
		super(entityPathInfo);
	}

	public EntityGridUIBuilder initListgridBuilder(String...excludeFields){
		EntityGridUIBuilder listgridBuilder = newEntityGridUIBuilder();
		listgridBuilder.buildCheckbox().buildForm(entityPathInfo.getBatchDeletePathInfo().getMethod(), entityPathInfo.getBatchDeletePathInfo().getPath());
		listgridBuilder.buildForEntity(excludeFields);
//		listgridBuilder.buildGridOperationsWithCommons(entityPathInfo);
		listgridBuilder.buildColumn("操作", UI.editLink(entityPathInfo), UI.link("查看", entityPathInfo.getShowPathInfo().getPath()+"/tables"));
		listgridBuilder.buildGridToobarMenusWithCommons(entityPathInfo);
		return listgridBuilder;
	}

	
	public void build(){
		newFormBuilder = this.initNewFormUIBuilder();
		newFormBuilder.buildUIComponent();
		editFormBuilder = this.initEditFormUIBuilder();
		editFormBuilder.buildUIComponent();
		
		listgridBuilder = this.initListgridBuilder();
		listgridBuilder.buildUIComponent();
		
		showTablesGridBuilder = newEntityGridUIBuilder();
//		showTablesGridBuilder.buildForm(entityPathInfo.getBatchDeletePathInfo().getMethod(), entityPathInfo.getBatchDeletePathInfo().getPath());
		FormLinkButtonUI configMenu = new FormLinkButtonUI("config", "配置");
		configMenu.setHref("/codegen/config");
		configMenu.setDataMethod(FormMethod.post);
		showTablesGridBuilder.buildToolbarMenus(configMenu);
		FormCheckboxUI cb = UI.gridCheckbox("tables", "", ":this");
		FormCheckboxUI cball = UI.gridCheckboxAll();
		cball.setValue("");
		showTablesGridBuilder.buildColumn(cball, cb);
		
		DataGridColumnUI tableName = new DataGridColumnUI();
		tableName.setUivaluer(new SimpleComponentUIValuer(tableName));
		showTablesGridBuilder.buildDataGridColumn("表名", tableName);
		showTablesGridBuilder.buildPagination(false);
		showTablesGridBuilder.buildUIComponent();
//		DataGridUI grid = showTablesGridBuilder.buildUIComponent();
//		grid.getForm().addHidden("dbid", ":id");
	}
	
	public PageUI getShowTablesGridPage(Object data){
		build();
		DataGridUI dg = showTablesGridBuilder.retriveUIComponent();
		FormUI form = new FormUI("");
		form.addHidden("dbid", ":id");
		dg.setInitFormName(form.getName());
		form.getChildren().addChild(dg);
		return UI.page(form, data);
	}
}
