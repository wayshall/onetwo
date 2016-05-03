package org.onetwo.common.excel;

import java.io.FileInputStream;
import java.util.Map;

import org.onetwo.common.excel.interfaces.TemplateGenerator;
import org.onetwo.common.excel.interfaces.XmlTemplateGeneratorFactory;
import org.onetwo.common.excel.utils.ExcelUtils;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;

abstract public class AbstractWorkbookTemplateFactory implements XmlTemplateGeneratorFactory, ModelGeneratorFactory {
	
	protected boolean cacheTemplate;
//	private final StringFreemarkerTemplateConfigurer parser;
	
	protected AbstractWorkbookTemplateFactory(boolean cacheTemplate) {
		super();
		this.cacheTemplate = cacheTemplate;
//		this.parser = new StringFreemarkerTemplateConfigurer();
//		parser.initialize();
	}

	@Override
	public TemplateGenerator create(String template, Map<String, Object> context) {
//		return DefaultExcelGeneratorFactory.createWorkbookGenerator(template, context);
		return create(getWorkbookModel(template, cacheTemplate), context);
	}
	
	@Override
	public TemplateGenerator create(WorkbookModel workbook, Map<String, Object> context){
		TemplateGenerator generator = new WorkbookExcelGeneratorImpl(workbook, context);
		return generator;
	}
	
	protected WorkbookModel readAsWorkbookModel(Resource config){
		PoiModel m = null;
		try {
			XStream xstream = ExcelUtils.registerExcelModel();
//			return ExcelUtils.readAsWorkbookModel(resource);
			/*String path = config.getFile().getPath();
			if(!parser.isMapped(path)){
				parser.map(path, config.getFile());
			}
			String content = parser.parse(config.getFile().getPath(), Collections.EMPTY_MAP);*/
			
			if(config.exists()){
				m = (PoiModel)xstream.fromXML(new FileInputStream(config.getFile()));
			}else{
				m = (PoiModel)xstream.fromXML(config.getInputStream());
			}
		} catch (Exception e) {
			throw ExcelUtils.wrapAsUnCheckedException("读取模板["+config+"]配置出错：" + e.getMessage(), e);
		}
		
		WorkbookModel model = null;
		if(TemplateModel.class.isInstance(m)){
			model = new WorkbookModel();
			model.addSheet((TemplateModel)m);
		}else{
			model = (WorkbookModel) m;
		}
		model.initModel();
		
		return model;
	}

	@Override
	public boolean checkTemplate(String template) {
		return getWorkbookModel(template, cacheTemplate)!=null;
	}

	public boolean isCacheTemplate() {
		return cacheTemplate;
	}

	public void setCacheTemplate(boolean cacheTemplate) {
		this.cacheTemplate = cacheTemplate;
	}

	abstract protected Resource getResource(String template) ;

	abstract protected WorkbookModel getWorkbookModel(String templatePath, boolean checkCache);
	
}


