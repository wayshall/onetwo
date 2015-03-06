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

/***
 * 为了避免与jasper冲突，foarmt为jfxls
 * {@link ExtJasperReportsXlsView}
 * @author weishao
 *
 */
public class JFishExcelView extends AbstractJFishExcelView {
	
	public static final String TEMPLATE_SUFFIX = ".xml";
	
	private XmlTemplateGeneratorFactory xmlTemplateExcelFactory;

	public JFishExcelView(){
		this.setSuffix(TEMPLATE_SUFFIX);
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
				
				String format = "."+generator.getFormat();//.xlsx
				downloadFileName = (downloadFileName.endsWith(".xls") || downloadFileName.endsWith(".xlsx"))?downloadFileName:(downloadFileName+format);
//				response.setContentType(RESPONSE_CONTENT_TYPE); 
//				response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
				this.setReponseHeader(downloadFileName, request, response);

				out = response.getOutputStream();
				generator.generateIt();
				generator.write(out);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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


	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return this.xmlTemplateExcelFactory.checkTemplate(getTemplatePath());
	}

	public void setXmlTemplateExcelFactory(XmlTemplateGeneratorFactory xmlTemplateExcelFactory) {
		this.xmlTemplateExcelFactory = xmlTemplateExcelFactory;
	}
	
}
