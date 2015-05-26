package org.onetwo.common.spring.web.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.slf4j.Logger;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/***
 * 为了避免与jasper冲突，foarmt为jfxls
 * {@link ExtJasperReportsXlsView}
 * @author weishao
 *
 */
abstract public class AbstractJFishExcelView extends AbstractUrlBasedView {
	
	public static final String FILENAME_KEY = "fileName";
	public static final String RESPONSE_CONTENT_TYPE = "application/download; charset=GBK";
	public static final String DEFAULT_CONTENT_TYPE = "application/jfxls";//

	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private String fileName;
	private String suffix;
	
	public AbstractJFishExcelView(){
	}
	
	protected String getDownloadFileName(HttpServletRequest request, Map<String, Object> model){
		return JFishWebUtils.getDownloadFileName(request, model, fileName);
	}
	
	protected String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, boolean encode){
		return JFishWebUtils.getDownloadFileName(request, model, fileName, encode);
	}

	protected void setReponseHeader(String downloadFileName, HttpServletRequest request, HttpServletResponse response){
		response.setContentType(RESPONSE_CONTENT_TYPE); 
		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
	}
	
	protected String getTemplatePath(){
		String template = getUrl();
		if(template==null)
			throw new JFishException("you must set a template in the model.");
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
