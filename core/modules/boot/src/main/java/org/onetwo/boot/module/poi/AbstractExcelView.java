package org.onetwo.boot.module.poi;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/***
 * 为了避免与jasper冲突，foarmt为jfxls
 * {@link ExtJasperReportsXlsView}
 * @author weishao
 *
 */
abstract public class AbstractExcelView extends AbstractUrlBasedView {
	
	public static final String FILENAME_KEY = "fileName";
	public static final String RESPONSE_CONTENT_TYPE = "application/download; charset=GBK";
	public static final String DEFAULT_CONTENT_TYPE = "application/jfxls";//


	public static String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, String defaultFileName){
		return getDownloadFileName(request, model, defaultFileName, true);
	}
	
	public static String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, String defaultFileName, boolean encode){
		String downloadFileName = request.getParameter("fileName");
		if(StringUtils.isBlank(downloadFileName)){
			//在model里的，由用户自己转码
			downloadFileName = (model!=null && model.containsKey("fileName"))?model.get("fileName").toString():defaultFileName;
		}else{
			if(encode){
				downloadFileName = LangUtils.changeCharset(downloadFileName, "GBK", "ISO8859-1");
			}
			/*try {
				downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO8859-1");
			} catch (Exception e) {
				throw new BaseException("get down file name error: " +e.getMessage());
			}*/
		}
//		downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO8859-1");
		return downloadFileName;
	}

	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private String fileName;
	private String suffix;
	
	public AbstractExcelView(){
	}
	

	public static String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, boolean encode) {
		return getDownloadFileName(request, null, "excel-export-filename", encode);
	}
	
	protected void setReponseHeader(String downloadFileName, HttpServletRequest request, HttpServletResponse response){
		response.setContentType(RESPONSE_CONTENT_TYPE); 
		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
	}
	
	protected String getTemplatePath(){
		String template = getUrl();
		if(template==null)
			throw new BaseException("you must set a template in the model.");
		if(!template.endsWith(getSuffix())){
			template += getSuffix();
		}
		return template;
	}

	public String getContentType() {
		return DEFAULT_CONTENT_TYPE;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/*@Override
	public boolean checkResource(Locale locale) throws Exception {
		return this.xmlTemplateExcelFactory.checkTemplate(getTemplatePath());
	}

	public void setXmlTemplateExcelFactory(XmlTemplateGeneratorFactory xmlTemplateExcelFactory) {
		this.xmlTemplateExcelFactory = xmlTemplateExcelFactory;
	}*/
	
}
