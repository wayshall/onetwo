package org.onetwo.common.excel.view.jsp;

import org.onetwo.common.spring.web.mvc.view.JsonExcelView;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.common.web.view.jsp.grid.GridTagBean;

public class ExportableGridTagBean extends GridTagBean {
	
	private boolean exportable;
	private String exportDataSource;
	private String exportJsonTemplate;
	
	public String getXlsFormatAction(){
		String action = getActionWithQueryString();
		action = TagUtils.appendParam(action, "format", JsonExcelView.URL_POSFIX);
		action = TagUtils.appendParam(action, "fileName", getTitle());
		action = TagUtils.appendParam(action, JsonExcelView.EXPORT_JSON_PARAM_NAME, LangUtils.encodeUrl(getExportJsonTemplate()));
		return action;
	}
	
	public boolean isExportable() {
		return exportable;
	}

	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}

	public String getExportDataSource() {
		return exportDataSource;
	}

	public void setExportDataSource(String exportDataSource) {
		this.exportDataSource = exportDataSource;
	}

	public String getExportJsonTemplate() {
		return exportJsonTemplate;
	}

	public void setExportJsonTemplate(String exportJsonTemplate) {
		this.exportJsonTemplate = exportJsonTemplate;
	}
}
