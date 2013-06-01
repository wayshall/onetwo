package org.onetwo.common.web.s2.ext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.onetwo.common.excel.PoiExcelGenerator;
import org.onetwo.common.excel.ExcelGeneratorFactory;
import org.onetwo.common.excel.TemplateModel;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.web.config.SiteConfig;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;

@SuppressWarnings( { "unchecked", "serial" })
public class ExcelResult extends StrutsResultSupport {
	public static final String DEFAULT_CONTENT_TYPE = "application/download; charset=GBK";
	public static final String TEMPLATE_DIR = "excel";
	
	protected String templatePath;
	protected String exportData;
	protected Object datas;
	
	/*@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		
		if (!(action instanceof Exportable))
			return ;

		Exportable excelable = (Exportable) action;
		if (!excelable.isExport() || excelable.getExportData() == null)
			return ;

		String templatePath = excelable.getExcelTemplatePath();
		exportExcel(templatePath, excelable, invocation);
		
	}*/
	
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		ActionProxy proxy = invocation.getProxy();
		templatePath = conditionalParse(templatePath, invocation);
		datas = invocation.getStack().findValue(exportData);
		
		if(StringUtils.isBlank(templatePath))
			templatePath = MyUtils.append(TEMPLATE_DIR, proxy.getNamespace(), "/", proxy.getActionName(), "-", proxy.getMethod(), ".xml");
		TemplateModel template = ExcelGeneratorFactory.getTemplateModel(templatePath, SiteConfig.getInstance().isProduct());

		OutputStream out = null;
		Map context = null;
		
		if(datas instanceof Map){
			context = (Map) datas;
		}else{
			context = new HashMap();
			context.put("data", datas);
		}
		
		HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
		try {
			response.setContentType(DEFAULT_CONTENT_TYPE); 
			String name = new String(template.getName().getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xls");

			out = response.getOutputStream();
			PoiExcelGenerator g = ExcelGeneratorFactory.createWebExcelGenerator(template, context);
			g.generateIt();
			g.write(out);
		} catch (Exception e) {
			response.reset();
			throw new ServiceException("export excel error!", e);
		} finally {
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

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public void setExportData(String exportData) {
		this.exportData = exportData;
	}
}
