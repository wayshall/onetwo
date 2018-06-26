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
	public static final String RESPONSE_CONTENT_TYPE = "application/download; charset=";
	public static final String DEFAULT_CONTENT_TYPE = "application/jfxls";//



	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private String fileNameField = FILENAME_KEY;
	private String charset = "GBK";
	private String suffix;
	
	public AbstractExcelView(){
	}
	

	public String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, String defaultFileName){
		return getDownloadFileName(request, model, defaultFileName, true);
	}
	
	public String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, String defaultFileName, boolean encode){
		String downloadFileName = request.getParameter(fileNameField);
		if(StringUtils.isBlank(downloadFileName)){
			//在model里的，由用户自己转码
			downloadFileName = (model!=null && model.containsKey(fileNameField))?model.get(fileNameField).toString():defaultFileName;
		}else{
			/*if(encode){
//				downloadFileName = LangUtils.changeCharset(downloadFileName, "GBK", "ISO8859-1");
				downloadFileName = LangUtils.changeCharset(downloadFileName, charset, "ISO8859-1");
			}*/
			/*try {
				downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO8859-1");
			} catch (Exception e) {
				throw new BaseException("get down file name error: " +e.getMessage());
			}*/
		}
		if(encode){
//			downloadFileName = LangUtils.changeCharset(downloadFileName, charset, "ISO8859-1");
			downloadFileName = LangUtils.encodeUrl(downloadFileName);
		}
//		downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO8859-1");
		return downloadFileName;
	}
	
	public String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, boolean encode) {
		return getDownloadFileName(request, model, "excel-export-filename", encode);
	}
	
	protected void setReponseHeader(String downloadFileName, HttpServletRequest request, HttpServletResponse response){
//		response.setContentType(RESPONSE_CONTENT_TYPE+charset); 
//		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
		response.setContentType("application/octet-stream"); 
		response.setHeader("Content-Disposition", "attachment;filename*=utf-8''"+downloadFileName);
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

	public String getFileNameField() {
		return fileNameField;
	}

	public void setFileNameField(String fileNameField) {
		this.fileNameField = fileNameField;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
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
