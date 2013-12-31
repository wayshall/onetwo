package org.onetwo.project.batch.tools.service;

import java.util.List;
import java.util.Map;

import org.onetwo.common.excel.DefaultExcelGeneratorFactory;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.project.batch.tools.entity.PsamEntity;
import org.springframework.batch.item.ItemWriter;

public class PsamWriter implements ItemWriter<PsamEntity>{

	@Override
	public void write(List<? extends PsamEntity> items) throws Exception {
		Map<String, Object> context = LangUtils.newHashMap();
		context.put("psamList", items);
		TemplateGenerator g = DefaultExcelGeneratorFactory.createWorkbookGenerator("tools/psam.xml", context);
		String path = "D:/mydev/workspace/onetwo/labs/projects/batch/src/test/resources/psam.xls";
		g.generateTo(path);
	}
	
	

}
