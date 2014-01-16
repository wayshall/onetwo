package org.onetwo.common.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode.ServiceErrorCode;
import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.util.WebUtils;

public class ExcelView extends AbstractView implements InitializingBean {
	
	private final static Logger logger = MyLoggerFactory.getLogger(ExcelView.class);
	
	public final static String DEFAULT_CONTENT_TYPE = "application/msexcel";
	
	private JsonMapper jsonMapper = JsonMapper.ignoreEmpty();
	
	private ModelGeneratorFactory modelGeneratorFactory;
	
	public ExcelView(){
		this.setContentType(DEFAULT_CONTENT_TYPE);
	}
	
	

	@Override
	public void afterPropertiesSet() throws Exception {
//		if(modelGeneratorFactory==null)
//			modelGeneratorFactory = SpringApplication.getInstance().getBean(ModelGeneratorFactory.class);
		Assert.notNull(modelGeneratorFactory, "excel factory must be set!");
	}



	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String expertJson = request.getParameter("exporter");
		WorkbookModel workbook = jsonMapper.fromJson(expertJson, WorkbookModel.class);
		if(workbook==null){
			logger.error("build excel model error : {}", expertJson);
			throw new BaseException("build excel model error !");
		}

		OutputStream out = null;
		try {
			String fileName = workbook.getSheet(0).getLabel();
			String requestUri = JFishWebUtils.requestUri();
			String filename = StringUtils.isBlank(fileName)?WebUtils.extractFullFilenameFromUrlPath(requestUri):fileName;
			response.setContentType(getContentType()); 
			filename = new String(filename.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");

			out = response.getOutputStream();
			Map<String, Object> context = new HashMap<String, Object>(model);
			TemplateGenerator templateGenerator = this.modelGeneratorFactory.create(workbook, context);
			templateGenerator.generateIt();
			templateGenerator.write(response.getOutputStream());
				
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


	public void setModelGeneratorFactory(ModelGeneratorFactory modelGeneratorFactory) {
		this.modelGeneratorFactory = modelGeneratorFactory;
	}

}
