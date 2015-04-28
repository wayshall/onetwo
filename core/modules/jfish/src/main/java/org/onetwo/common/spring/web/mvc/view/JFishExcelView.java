package org.onetwo.common.spring.web.mvc.view;

import java.io.File;
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
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.RandUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.util.Assert;

/***
 * 为了避免与jasper冲突，foarmt为jfxls
 * {@link ExtJasperReportsXlsView}
 * @author weishao
 *
 */
public class JFishExcelView extends AbstractJFishExcelView {
	
	public static final String TEMPLATE_SUFFIX = ".xml";
	
	private XmlTemplateGeneratorFactory xmlTemplateExcelFactory;
	private BaseSiteConfig siteConfig = BaseSiteConfig.getInstance();

	public JFishExcelView(){
		this.setSuffix(TEMPLATE_SUFFIX);
	}
	

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(xmlTemplateExcelFactory==null){
			xmlTemplateExcelFactory = SpringApplication.getInstance().getBean(XmlTemplateGeneratorFactory.class);
			Assert.notNull(xmlTemplateExcelFactory, "there is not excel xml template factory config.");
		}
		
		String downloadFileName = getDownloadFileName(request, model);
		TemplateGenerator generator = createTemplateGenerator(model);//this.xmlTemplateExcelFactory.create(template.toString(), model);
		String format = "."+generator.getFormat();//.xlsx
		String saveFileName = (downloadFileName.endsWith(".xls") || downloadFileName.endsWith(".xlsx"))?downloadFileName:(downloadFileName+format);
		
//		response.setContentType(RESPONSE_CONTENT_TYPE); 
//		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
		this.setReponseHeader(saveFileName, request, response);

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			this.writeFileToResponse(generator, saveFileName, format, out);
			
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
	
	protected void writeFileToResponse(TemplateGenerator generator, String downloadFileName, String format, OutputStream out) throws IOException{
		int size = generator.generateIt();
		logger.info("excel generate size: {}", size);
		
		if(siteConfig.isViewExcelGeneratedFile() || size<siteConfig.getViewExcelGeneratedFileThredshold()){
			generator.write(out);
		}else{
			String dir = siteConfig.getViewExcelGeneratedFileDir();
			FileUtils.makeDirs(dir);
			String filePath = dir + "/" + downloadFileName + "-" + NiceDate.New().format("yyyyMMddHHmmssSSS") + "-" + RandUtils.randomString(5) + format;
			logger.info("write excel to : {}", dir + "/" + JFishWebUtils.getDownloadFileName(false));
			
			/*TimeCounter t = TimeCounter.create("write excel");
			t.start();*/
			File file = generator.write(filePath);
//			t.stop();
			
//			t.restart("copy to repsonse");
			FileUtils.copyFileToOutputStream(out, file);
//			t.stop();
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
