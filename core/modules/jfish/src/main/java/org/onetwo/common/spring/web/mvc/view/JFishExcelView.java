package org.onetwo.common.spring.web.mvc.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode.ServiceErrorCode;
import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class JFishExcelView extends AbstractUrlBasedView {
	public static final String FILENAME_KEY = "fileName";
	public static final String TEMPLATE_SUFFIX = ".xml";
	public static final String RESPONSE_CONTENT_TYPE = "application/download; charset=GBK";
	public static final String DEFAULT_CONTENT_TYPE = "application/xls";
	
	private XmlTemplateGeneratorFactory xmlTemplateExcelFactory;

	private String fileName;
	private String suffix = TEMPLATE_SUFFIX;
	
	public JFishExcelView(){
	}
	
	protected String getDownloadFileName(HttpServletRequest request, Map<String, Object> model) throws Exception{
		return JFishWebUtils.getDownloadFileName(request, model, fileName);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(xmlTemplateExcelFactory==null){
			xmlTemplateExcelFactory = SpringApplication.getInstance().getBean(XmlTemplateGeneratorFactory.class);
			if(xmlTemplateExcelFactory==null){
				throw new JFishException("there is not excel xml template factory config.");
			}
		}

		OutputStream out = null;
		
		boolean browse = "true".equals(JFishWebUtils.req("browse"));
		String downloadFileName = getDownloadFileName(request, model);
		
		try {
//			Object template = model.get(TEMPLATE_KEY);
//			String template = getTemplatePath();
			TemplateGenerator generator = createTemplateGenerator(model);//this.xmlTemplateExcelFactory.create(template.toString(), model);
			if(browse){
				//TODO
			}else{
//				String requestUri = JFishWebUtils.requestUri();
				//WebUtils.extractFullFilenameFromUrlPath(requestUri)
				
				response.setContentType(RESPONSE_CONTENT_TYPE); 
				downloadFileName = (downloadFileName.endsWith("xls") || downloadFileName.endsWith("xlsx"))?downloadFileName:(downloadFileName+".xls");
				response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);

				out = response.getOutputStream();
				generator.generateIt();
				generator.write(out);
			}
		} catch (Exception e) {
			if(LangUtils.isError(e, ServiceErrorCode.RESOURCE_NOT_FOUND)){
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}else{
				response.reset();
				throw new JFishException("export excel error!", e);
			}
		}finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					response.reset();
					throw new ServiceException("export excel error!", e);
				}
			}
		}
	}
	
	protected TemplateGenerator createTemplateGenerator(Map<String, Object> model){
		String template = getTemplatePath();
		return this.xmlTemplateExcelFactory.create(template.toString(), model);
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
		return "application/jfxls";
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

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return this.xmlTemplateExcelFactory.checkTemplate(getTemplatePath());
	}

	public void setXmlTemplateExcelFactory(XmlTemplateGeneratorFactory xmlTemplateExcelFactory) {
		this.xmlTemplateExcelFactory = xmlTemplateExcelFactory;
	}
	
}
