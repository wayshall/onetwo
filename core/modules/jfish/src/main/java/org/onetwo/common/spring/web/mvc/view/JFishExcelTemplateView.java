package org.onetwo.common.spring.web.mvc.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.excel.etemplate.ETemplateContext;
import org.onetwo.common.excel.etemplate.ExcelTemplateGenerator;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode.ServiceErrorCode;
import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.io.Resource;

public class JFishExcelTemplateView extends AbstractJFishExcelView {

	public JFishExcelTemplateView(){
		this.setSuffix(".xlsx");
	}
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		OutputStream out = null;
		try {

			String downloadFileName = getDownloadFileName(request, model);
			Resource template = getTemplateResource();
			String format = FileUtils.getExtendName(template.getURL().toString(), true);
			downloadFileName = (downloadFileName.endsWith(".xls") || downloadFileName.endsWith(".xlsx"))?downloadFileName:(downloadFileName+format);
			this.setReponseHeader(downloadFileName, request, response);
			
			out = response.getOutputStream();

			ExcelTemplateGenerator g = new ExcelTemplateGenerator(template.getFile());
			ETemplateContext context = new ETemplateContext();
			context.putAll(model);

			g.generate(context, out);
			
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
	@Override
	public boolean checkResource(Locale locale) throws Exception {
		Resource template = getTemplateResource();
		return template!=null && template.exists();
	}
	
	public Resource getTemplateResource(){
		String path = getTemplatePath();
		Resource template = SpringApplication.getInstance().getAppContext().getResource(path);
		return template;
	}
	

}
