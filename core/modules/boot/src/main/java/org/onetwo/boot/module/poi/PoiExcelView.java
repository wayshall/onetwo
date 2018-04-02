package org.onetwo.boot.module.poi;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.date.NiceDate;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.RandUtils;
import org.onetwo.ext.poi.excel.interfaces.TemplateGenerator;
import org.onetwo.ext.poi.excel.interfaces.XmlTemplateGeneratorFactory;
import org.springframework.util.Assert;

/***
 * 为了避免与jasper冲突，foarmt为jfxls
 * {@link ExtJasperReportsXlsView}
 * @author weishao
 *
 */
public class PoiExcelView extends AbstractExcelView {
	
	public static final String TEMPLATE_SUFFIX = ".xml";
	
	private XmlTemplateGeneratorFactory xmlTemplateExcelFactory;
	private PoiProperties poiProperties;

	public PoiExcelView(){
		this.setSuffix(TEMPLATE_SUFFIX);
	}
	

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(xmlTemplateExcelFactory==null){
			xmlTemplateExcelFactory = Springs.getInstance().getBean(XmlTemplateGeneratorFactory.class);
			Assert.notNull(xmlTemplateExcelFactory, "there is not excel xml template factory config.");
		}
		if(poiProperties==null){
			poiProperties = Springs.getInstance().getBean(PoiProperties.class);
			Assert.notNull(poiProperties, "poiProperties can not be found.");
		}
		
		String downloadFileName = getDownloadFileName(request, model, true);
		TemplateGenerator generator = createTemplateGenerator(model);//this.xmlTemplateExcelFactory.create(template.toString(), model);
		String format = "."+generator.getFormat();//.xlsx
		String saveFileName = (downloadFileName.endsWith(".xls") || downloadFileName.endsWith(".xlsx"))?downloadFileName:(downloadFileName+format);
		
//		response.setContentType(RESPONSE_CONTENT_TYPE); 
//		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
		this.setReponseHeader(saveFileName, request, response);

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			
			if(poiProperties.isWriteToLocal()){
				String dir = poiProperties.getLocalDir();
				FileUtils.makeDirs(dir);
				String filePath = dir + "/" + downloadFileName + "-" + NiceDate.New().format("yyyyMMddHHmmssSSS") + "-" + RandUtils.randomString(5) + format;
				logger.info("write excel to : {}", dir + getDownloadFileName(request, model, false));
				
				File file = generator.write(filePath);
				
				FileUtils.copyFileToOutputStream(out, file);
			}else{
				generator.write(out);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.reset();
			throw new ServiceException("export excel error!", e);
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
