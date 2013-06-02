package org.onetwo.common.excel;

import java.util.List;

import org.onetwo.common.utils.Freezer;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class TemplateModel {
	private static final String DEFAULLT_VARNAME = "_sheet";

	private String name;
	private String varname;
	
	private List<RowModel> rows;
	
	private final Freezer freezer = new Freezer(TemplateModel.class.getSimpleName());
	
	//new
	private String label;
	private String datasource;
	private Integer sizePerSheet;
	
	public TemplateModel(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.varname = name;
	}

	public List<RowModel> getRows() {
		return rows;
	}

	public void setRows(List<RowModel> rows) {
		this.freezer.checkOperation("setRows");
		this.rows = rows;
	}

	public Freezer getFreezer() {
		return freezer;
	}

	public String getLabel() {
		if(StringUtils.isBlank(label))
			return getName();
		return label;
	}
	
	public String getVarName(){
		if(StringUtils.isBlank(varname))
			varname = name;
		if(StringUtils.isBlank(varname) || !LangUtils.isWord(varname))
			varname = DEFAULLT_VARNAME;
		return varname;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public Integer getSizePerSheet() {
		return sizePerSheet;
	}

	public void setSizePerSheet(Integer sizePerSheet) {
		this.sizePerSheet = sizePerSheet;
	} 
	
	public boolean isMultiSheet(){
		return this.sizePerSheet!=null && this.sizePerSheet>0;
	}

}
