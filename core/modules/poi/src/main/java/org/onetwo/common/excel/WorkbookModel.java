package org.onetwo.common.excel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.excel.utils.ExcelUtils;

import com.google.common.collect.Lists;

public class WorkbookModel implements PoiModel {
	private String listener;
	
	private List<TemplateModel> sheets;
	
	private List<VarModel> vars;
	
	private String format = FORMAT_XLS;
	
	@Override
	public void initModel(){
		for(TemplateModel template : sheets){
			 template.initModel();
		}
	}

	public List<TemplateModel> getSheets() {
		return sheets;
	}

	public void setSheets(List<TemplateModel> sheets) {
		this.sheets = sheets;
	}
	public void addSheet(TemplateModel sheet){
		if(sheets==null)
			sheets = Lists.newArrayList();
		this.sheets.add(sheet);
	}
	public TemplateModel getSheet(int index){
		return this.sheets.get(index);
	}

	public String getListener() {
		return listener;
	}

	public void setListener(String setter) {
		this.listener = setter;
	}

	public List<VarModel> getVars() {
		return ExcelUtils.emptyIfNull(vars);
	}

	public void setVars(List<VarModel> vars) {
		this.vars = vars;
	}

	public String getFormat() {
		return StringUtils.isBlank(format)?FORMAT_XLS:format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
}
