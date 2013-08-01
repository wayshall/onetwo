package org.onetwo.plugins.fmtagext.ui.datagrid;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.UI.TemplateKeys;
import org.onetwo.plugins.fmtagext.ui.valuer.SimpleComponentUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;
import org.springframework.util.Assert;

public class DataGridRowUI extends FmUIComponent {

	private List<DataGridColumnUI> columns = LangUtils.newArrayList();
	
	public DataGridRowUI(DataGridUI parent) {
		super(parent, TemplateKeys.UI_GRID_ROW);
	}

	public DataGridRowUI addColumns(Object... columns){
		Assert.notEmpty(columns);
		for(Object col : columns){
			addColumnByObject(col);
		}
		return this;
	}
	
	public DataGridColumnUI addColumnByObject(Object column){
		DataGridColumnUI dgCol = null;
		if(String.class.isInstance(column)){
			dgCol = new DataGridColumnUI(column.toString());
		}else if(DataGridColumnUI.class.isInstance(column)){
			dgCol = (DataGridColumnUI) column;
		}else if(FmUIComponent.class.isInstance(column)){
			dgCol = new DataGridColumnUI((FmUIComponent)column);
		}else if(UIValuer.class.isInstance(column)){
			dgCol = new DataGridColumnUI();
			dgCol.setUivaluer((UIValuer<?>)column);
		}else{
			throw new IllegalArgumentException("unsupported this column type:" + column.getClass());
		}
		addColumn(dgCol);
		return dgCol;
	}
	
	public void addColumn(DataGridColumnUI column){
		column.setParent(this);
		this.columns.add(column);
	}
	
	public void addColumnInnerUI(int index, FmUIComponent innerUI){
		DataGridColumnUI dgc = new DataGridColumnUI(innerUI);
		this.columns.add(index, dgc);
	}

	public List<DataGridColumnUI> getColumns() {
		return columns;
	}
	
	public DataGridRowUI setColumnCssStyles(String...headerCssStyle){
		if(LangUtils.isEmpty(headerCssStyle))
			return this;
		int index = 0;
		for(DataGridColumnUI dgcol : getColumns()){
			if(headerCssStyle.length>index){
				dgcol.setCssStyle(headerCssStyle[index]);
			}
			index++;
		}
		return this;
	}
	
	public DataGridRowUI setColumnCssClasses(String...headerCssClass){
		if(LangUtils.isEmpty(headerCssClass))
			return this;
		int index = 0;
		for(DataGridColumnUI dgcol : getColumns()){
			if(headerCssClass.length>index){
				dgcol.setCssStyle(headerCssClass[index]);
			}
			index++;
		}
		return this;
	}

	protected UIValuer<?> createUIValuer(){
		return new SimpleComponentUIValuer(this, value);
	}

}
