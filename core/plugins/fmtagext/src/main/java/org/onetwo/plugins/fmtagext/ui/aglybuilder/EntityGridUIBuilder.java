package org.onetwo.plugins.fmtagext.ui.aglybuilder;

import org.onetwo.common.spring.rest.RestPather.EntityPathInfo;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtag.JFieldShowable;
import org.onetwo.plugins.fmtagext.ui.ContainerUIComponent;
import org.onetwo.plugins.fmtagext.ui.DataGridUIBuilder;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.JFieldViewObject;
import org.onetwo.plugins.fmtagext.ui.UI;
import org.onetwo.plugins.fmtagext.ui.UIComponent;
import org.onetwo.plugins.fmtagext.ui.ViewEntry;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridColumnUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridHeaderRowUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridIteratorRowUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.MenuBarUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.PaginationFooterUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI;

public class EntityGridUIBuilder extends BaseEntityUIBuilder<DataGridUI> implements DataGridUIBuilder {
	
	protected DataGridUI datagrid;
	private DataGridHeaderRowUI headerRow;
	private DataGridIteratorRowUI iteratorRow;
//	private boolean built;
//	private int columnOrder = -1;
//	private boolean sortedColumn;
	private boolean pagination = true;

	public EntityGridUIBuilder(Class<?> entityClass) {
		this(entityClass, "");
	}
	public EntityGridUIBuilder(Class<?> entityClass, String title) {
		super(entityClass);
		this.datagrid = new DataGridUI(title);
		this.headerRow = new DataGridHeaderRowUI(datagrid);
		this.iteratorRow = new DataGridIteratorRowUI(datagrid);
	}

	public EntityGridUIBuilder buildCheckbox(){
//		int checkboxOrder = columnOrder;
		headerRow.addColumnByObject(UI.gridCheckboxAll());
		iteratorRow.addColumnByObject(UI.gridCheckbox("ids", "", FmUIComponent.asProperty("id")));
		return this;
	}

	public EntityGridUIBuilder buildColumn(Object headerColumn, UIComponent... uiComponents){
		if(LangUtils.isEmpty(uiComponents))
			return this;
		
//		int opOrder = showOrder;
		DataGridColumnUI operationHeader = headerRow.addColumnByObject(headerColumn);
//		operationHeader.setShowOrder(opOrder);
		
		ContainerUIComponent container = UI.container();
		for(UIComponent uic :uiComponents){
			container.addChild(uic);
		}
		
		DataGridColumnUI operation = new DataGridColumnUI(container);
//		operation.setShowOrder(opOrder);
		iteratorRow.addColumn(operation);
		
		return this;
	}
	
	public EntityGridUIBuilder buildDataGridColumn(Object headerColumn, DataGridColumnUI operation){
		headerRow.addColumnByObject(headerColumn);
		iteratorRow.addColumn(operation);
		return this;
	}

	public EntityGridUIBuilder buildForm(String method, String action) {
		FormUI form = new FormUI(datagrid, "dataForm", "").setFormAction(method, action);
		datagrid.setForm(form);
		return this;
	}
	public EntityGridUIBuilder buildToolbarMenus(UIComponent... toolbarMenus) {
		if(!LangUtils.isEmpty(toolbarMenus)){
			datagrid.addToolbar(new MenuBarUI(toolbarMenus));
		}
		return this;
	}

	public EntityGridUIBuilder buildHeaderCssStyle(String... headerCssStyle) {
		headerRow.setColumnCssStyles(headerCssStyle);
		return this;
	}

	public EntityGridUIBuilder buildHeaderCssClass(String... headerCssClass) {
		headerRow.setColumnCssClasses(headerCssClass);
		return this;
	}


	@Override
	public EntityGridUIBuilder buildForEntity(String...excludeFields) {
		this.beforeBuildEntityFields();
		this.buildEntityFields(excludeFields);
		this.afterBuildEntityFields();
		return this;
	}
	
	private void buildEntityFields(String... excludeFields) {
		ViewEntry entry = this.getJFishViewEntry(entityClass);
		
		this._buildTitlte(datagrid, entry);
		
		for(JFieldViewObject field : entry.getFields()){
			if(!isAcceptedField(excludeFields, field.getName()))
				continue;
			if(!field.canShowIn(JFieldShowable.grid)){
				continue;
			}
			
			this.buildListGridField(datagrid, field);
//			if(col!=null)
//				iteratorRow.addColumn(col);
//			if(ArrayUtils.contains(searchFields, field.getName())){
//				dfield.search = true;
//			}
			
		}
		
	}
	
	public void buildListGridField(DataGridUI datagrid, JFieldViewObject viewObj) {
		buildColumn(viewObj.getLabel(), viewObj.getShowValue(), viewObj.getOrder());
	}

	
	public void buildGridToobarMenusWithCommons(EntityPathInfo entityPathInfo, UIComponent... toolbarMenus){
		UIComponent[] commons = new UIComponent[]{UI.newLink(entityPathInfo), UI.batchDeleteLink(entityPathInfo)};
		if(!LangUtils.isEmpty(toolbarMenus)){
			commons = (UIComponent[]) ArrayUtils.addAll(commons, toolbarMenus);
		}
		buildToolbarMenus(commons);
	}
	
	public void buildGridOperationsWithCommons(EntityPathInfo entityPathInfo, UIComponent... uiComponents){
		UIComponent[] commons = new UIComponent[]{UI.editLink(entityPathInfo), UI.showLink(entityPathInfo)};
		if(!LangUtils.isEmpty(uiComponents)){
			commons = (UIComponent[])ArrayUtils.addAll(commons, uiComponents);
		}
		buildColumn("操作", commons);
	}

	
	public DataGridUI buildUIComponent(){
		
		datagrid.addGridRow(headerRow);
		datagrid.addGridRow(iteratorRow);
		
		if(this.pagination && datagrid.getFooter()==null){
			datagrid.setFooter(new PaginationFooterUI(datagrid));
		}
		
		return this.datagrid;
	}
	
	public DataGridUI retriveUIComponent(){
		return this.datagrid;
	}
	
	/*protected void everyFieldForRow(DataGridRowUI headerRow, DataGridRowUI iteratorRow, JFieldViewObject viewObj){
		this.buildColumn(viewObj.getLabel(), viewObj.getShowValue(), viewObj.getOrder());
	}*/
	
	public EntityGridUIBuilder buildColumn(String labelString, String valueString, int order){
		//add header
		headerRow.addColumnByObject(labelString);
		//add iterator
		DataGridColumnUI col = new DataGridColumnUI(valueString);
//		col.setShowOrder(order);
		/*if(order>columnOrder){
			this.columnOrder = order;
		}*/
		iteratorRow.addColumn(col);
		return this;
	}
	public boolean isPagination() {
		return pagination;
	}
	public EntityGridUIBuilder buildPagination(boolean pagination) {
		this.pagination = pagination;
		return this;
	}
	
	
	
}
