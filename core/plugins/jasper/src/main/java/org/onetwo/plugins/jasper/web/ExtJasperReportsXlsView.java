package org.onetwo.plugins.jasper.web;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.springframework.web.servlet.view.jasperreports.JasperReportsXlsView;

/****
 * media-type.properties 里配置的format为xls
 * contentType: application/vnd.ms-excel
 * 
 * @author weishao
 *
 */
public class ExtJasperReportsXlsView  extends JasperReportsXlsView {

	
	public ExtJasperReportsXlsView(){
//		this.setContentType("application/msexcel");

		Map<JRExporterParameter, Object> temp = LangUtils.newHashMap();
		temp.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
		temp.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		temp.put(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);  
		temp.put(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		this.setConvertedExporterParameters(temp);
		
	}

	@Override
	protected JRDataSource convertReportData(Object value) throws IllegalArgumentException {
		if(value instanceof Page){
			Page<?> page = (Page<?>) value;
			return new JRBeanCollectionDataSource(page.getResult());
		}
		return super.convertReportData(value);
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	protected Class[] getReportDataTypes() {
		return new Class[] {Collection.class, Object[].class, Page.class};
	}
	
	protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response)throws Exception {
		if(!useWriter()){
			String downloadFileName = FileUtils.getFileNameWithoutExt(getUrl());
			downloadFileName = JFishWebUtils.getDownloadFileName(model, downloadFileName);
			downloadFileName = (downloadFileName.endsWith("xls") || downloadFileName.endsWith("xlsx"))?downloadFileName:(downloadFileName+".xls");
			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName );
		}
		
		super.renderReport(populatedReport, model, response);
	}

}
