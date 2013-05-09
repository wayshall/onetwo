package org.onetwo.plugins.fmtagext.ui.aglybuilder;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.spring.rest.RestPather.EntityPathInfo;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtag.JFieldShowable;
import org.onetwo.plugins.fmtagext.ui.ContainerUIComponent;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.JFieldViewObject;
import org.onetwo.plugins.fmtagext.ui.UI;
import org.onetwo.plugins.fmtagext.ui.UI.TemplateKeys;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridColumnUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridRowUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.PaginationFooterUI;
import org.onetwo.plugins.fmtagext.ui.UIComponent;
import org.onetwo.plugins.fmtagext.ui.ViewEntry;

public class ShowEntityGridUIBuilder extends BaseEntityUIBuilder<DataGridUI>implements EntityUIBuilder<DataGridUI>{

	private ContainerUIComponent container;
	protected DataGridUI datagrid;
//	private boolean built;
	
	public ShowEntityGridUIBuilder(Class<?> entityClass, String title) {
		super(entityClass);
		datagrid = new ShowDataGridUI(title);
		datagrid.setTemplate(TemplateKeys.UI_SHOW_GRID);
	}

	public ShowEntityGridUIBuilder buildToolbarMenus(UIComponent... uiComponent){
		for(UIComponent uic : uiComponent){
			this.datagrid.addToolbar(uic);
		}
		return this;
	}


	@Override
	public ShowEntityGridUIBuilder buildForEntity(String...excludeFields) {
		this.beforeBuildEntityFields();
		this.buildEntityFields(excludeFields);
		this.afterBuildEntityFields();
		return this;
	}
	
	private void buildEntityFields(String...excludeFields) {
		ViewEntry entry = this.getJFishViewEntry(entityClass);
		
		this._buildTitlte(datagrid, entry);
		
		if(container!=null){
			datagrid.setToolbar(container);
		}

//		List<JFishMappedField> gridFields = LangUtils.newArrayList()
		
		for(JFieldViewObject field : entry.getFields()){
			if(!isAcceptedField(excludeFields, field.getName()))
				continue;

			if(!field.canShowIn(JFieldShowable.show)){
				continue ;
			}
//			this.everyFieldForBuild(viewObj);
			buildShowGridField(datagrid, field);
			
		}
		
	}
	
	public DataGridUI buildUIComponent(){
		return datagrid;
	}
	
	public DataGridUI retriveUIComponent(){
		return this.datagrid;
	}
	
	public void buildShowGridField(DataGridUI datagrid, JFieldViewObject viewObj) {
		buildRow(viewObj.getLabel(), viewObj.getShowValue());
	}
	
	public ShowEntityGridUIBuilder buildShowGridToobarMenusWithCommons(EntityPathInfo entityPathInfo, UIComponent... toolbarMenus){
		UIComponent[] commons = new UIComponent[]{UI.editEntityButton(entityPathInfo), UI.backToButton(":backUrl")};
		if(!LangUtils.isEmpty(toolbarMenus)){
			commons = (UIComponent[])ArrayUtils.addAll(commons, toolbarMenus);
		}
		buildToolbarMenus(commons);
		return this;
	}
	
	public ShowEntityGridUIBuilder buildRow(String labelString, String valueString){
		DataGridRowUI row = new DataGridRowUI(datagrid);
		DataGridColumnUI label = new DataGridColumnUI(labelString);
		DataGridColumnUI value = new DataGridColumnUI(valueString);
		row.addColumns(label, value);
		
		datagrid.addGridRow(row);
		
		return this;
	}
	
	public ShowEntityGridUIBuilder buildRow(DataGridColumnUI label, DataGridColumnUI value){
		DataGridRowUI row = new DataGridRowUI(datagrid);
		row.addColumns(label, value);
		
		datagrid.addGridRow(row);
		
		return this;
	}
	
	private static class ShowDataGridUI extends DataGridUI {

		public ShowDataGridUI(String title) {
			super(title);
		}
		public void setFooter(FmUIComponent footer) {
			if(PaginationFooterUI.class.isInstance(footer))
				throw new JFishException("show data grid unsupported pagiation.");
			super.setFooter(footer);
		}
	}
	
}
