package org.onetwo.common.spring.web.mvc.view;

import java.io.IOException;
import java.io.OutputStream;
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
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.util.WebUtils;

public class JFishExcelView extends AbstractUrlBasedView {
	public static final String FILENAME_KEY = "fileName";
	public static final String TEMPLATE_POSTFIX = ".xml";
	public static final String DEFAULT_CONTENT_TYPE = "application/download; charset=GBK";
	
	private XmlTemplateGeneratorFactory xmlTemplateExcelFactory;
	
	private String fileName;
	
	public JFishExcelView(){
		this.xmlTemplateExcelFactory = SpringApplication.getInstance().getBean(XmlTemplateGeneratorFactory.class);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(xmlTemplateExcelFactory==null){
			throw new JFishException("there is not excel xml template factory config.");
		}

		OutputStream out = null;
		
		boolean browse = "true".equals(JFishWebUtils.req("browse"));
		
		try {
//			Object template = model.get(TEMPLATE_KEY);
			String template = getUrl();
			if(template==null)
				throw new JFishException("you must set a template in the model.");
			if(!template.endsWith(TEMPLATE_POSTFIX)){
				template += TEMPLATE_POSTFIX;
			}
			TemplateGenerator generator = this.xmlTemplateExcelFactory.create(template.toString(), model);
			if(browse){
				//TODO
			}else{
				String requestUri = JFishWebUtils.requestUri();
				String filename = StringUtils.isBlank(fileName)?WebUtils.extractFullFilenameFromUrlPath(requestUri):fileName;
				response.setContentType(getContentType()); 
//				filename = new String(filename.getBytes("GBK"), "ISO8859-1");
				response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");

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

	public String getContentType() {
		return DEFAULT_CONTENT_TYPE;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
