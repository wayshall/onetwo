package org.onetwo.common.spring.web.mvc.view;

import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.onetwo.common.excel.DefaultExcelGeneratorFactory;
import org.onetwo.common.excel.WorkbookModel;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;

public class JsonExcelView extends JFishExcelView {

	public static final String URL_POSFIX = "jfxls";
	public static final String EXPORT_JSON_PARAM_NAME = "exporter";

	@Override
	protected TemplateGenerator createTemplateGenerator(Map<String, Object> model) {
		String exportJson = getExportJson();
		if(StringUtils.isNotBlank(exportJson)){
			WorkbookModel workbook = JsonMapper.IGNORE_EMPTY.fromJson(exportJson, WorkbookModel.class);
			Assert.assertNotNull(workbook);
			TemplateGenerator g = DefaultExcelGeneratorFactory.createWorkbookGenerator(workbook, model);
			return g;
		}
		return super.createTemplateGenerator(model);
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		String exportJson = getExportJson();
		if(StringUtils.isNotBlank(exportJson))
			return true;
		return super.checkResource(locale);
	}
	
	protected String getExportJson(){
		return JFishWebUtils.request().getParameter(EXPORT_JSON_PARAM_NAME);
	}

}
