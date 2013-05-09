package org.onetwo.plugins.fmtagext.ui.datagrid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.ui.ContainerUIComponent;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.UI;
import org.onetwo.plugins.fmtagext.ui.UI.TemplateKeys;
import org.onetwo.plugins.fmtagext.ui.UIComponent;
import org.onetwo.plugins.fmtagext.ui.form.FormUI;
import org.onetwo.plugins.fmtagext.ui.valuer.SimpleComponentUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;

public class DataGridUI extends FmUIComponent {

	private FormUI form;
	private FmUIComponent searchBar;
	private ContainerUIComponent toolbar;
//	private DataGridRowUI iteratorRow;
	
	private List<DataGridRowUI> rows = LangUtils.newArrayList();
	
	protected FmUIComponent footer;
	
	private int colspan;
	
//	private String formAction;
	
	public DataGridUI() {
		this("");
	}
	public DataGridUI(String title) {
		super(null, TemplateKeys.UI_GRID);
		this.title = title;
	}
	
	public DataGridUI addColumn(String...columns){
		Map<String, String> colMap = LangUtils.newHashMap((Object[])columns);
		addHeaderRow(colMap.values());
		addIteratorRow(colMap.keySet());
		return this;
	}

	
	public DataGridRowUI addRow(Object...columns){
		DataGridRowUI row = new DataGridRowUI(this);
		row.addColumns(columns);
		addGridRow(row);
		return row;
	}
	public DataGridHeaderRowUI addHeaderRow(Object...columns){
		DataGridHeaderRowUI row = new DataGridHeaderRowUI(this);
		row.addColumns(columns);
		addGridRow(row);
		return row;
	}
	
	public DataGridUI addGridRow(DataGridRowUI row){
		row.setParent(this);
		this.rows.add(row);
		return this;
	}
	public DataGridRowUI addRow(Collection<String> columns){
		return addRow(columns.toArray(new Object[columns.size()]));
	}
	
	public DataGridIteratorRowUI addIteratorRow(Object...columns){
//		Map<String, String> colMap = LangUtils.newHashMap((Object[])columns);
		DataGridIteratorRowUI row = new DataGridIteratorRowUI(this);
		row.addColumns(columns);
		addGridRow(row);
		return row;
	}
	
	public DataGridRowUI addIteratorRow(Collection<Object> columns){
		return addIteratorRow(columns.toArray(new Object[columns.size()]));
	}
	public FmUIComponent getSearchBar() {
		return searchBar;
	}
	public void setSearchBar(FmUIComponent searchBar) {
		this.searchBar = searchBar;
	}
	public ContainerUIComponent getToolbar() {
		return toolbar;
	}
	public void setToolbar(ContainerUIComponent toolbar) {
		this.toolbar = toolbar;
	}
	public DataGridUI addToolbar(UIComponent toolbar) {
		if(this.toolbar==null){
			this.toolbar = UI.container("&nbsp;");
		}
		this.toolbar.addChild(toolbar);
		return this;
	}
	public FmUIComponent getFooter() {
		return footer;
	}
	public void setFooter(FmUIComponent footer) {
		this.footer = footer;
		this.footer.setParent(this);
	}
	/*public DataGridRowUI getIteratorRow() {
		return iteratorRow;
	}*/
	public List<DataGridRowUI> getRows() {
		return rows;
	}
	public void setRows(List<DataGridRowUI> rows) {
		this.rows = rows;
	}
	
	public int getColspan() {
		if(colspan==0){
			for(DataGridColumnUI column : this.rows.get(0).getColumns()){
				colspan += column.getColspan();
			}
		}
		return colspan;
	}

	public FormUI getForm() {
		return form;
	}
	public void setForm(FormUI form) {
		this.form = form;
		this.form.setParent(this);
	}
	
	protected UIValuer<?> createUIValuer(){
		return new SimpleComponentUIValuer(this);
	}

	public String getTitle() {
		if(StringUtils.isBlank(title))
			return LangUtils.EMPTY_STRING;
		if(isProperty(title)){
			return createUIValuer(title, "").getUIValue(null).toString();
		}else if(isExpresstion(title)){
			return createUIValuer(title, "").getUIValue(null).toString();
		}
		return title;
	}
}
