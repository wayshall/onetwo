package org.onetwo.common.excel;

public interface Exportable {
	
	public String EXPORT_EXCEL = "export_excel";

	public String getExcelTemplatePath();
	
	public boolean isExport();
	
	public Object getExportData();

}
